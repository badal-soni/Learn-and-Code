package com.intimetec.newsaggregation.eventlistener;

import com.intimetec.newsaggregation.constant.EmailConstants;
import com.intimetec.newsaggregation.constant.NotificationType;
import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import com.intimetec.newsaggregation.dto.event.NewsFetchedEvent;
import com.intimetec.newsaggregation.entity.*;
import com.intimetec.newsaggregation.notification.email.EmailNotificationService;
import com.intimetec.newsaggregation.repository.KeywordsRepository;
import com.intimetec.newsaggregation.repository.NotificationConfigurationRepository;
import com.intimetec.newsaggregation.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsFetchedEventListener {

    private final NotificationConfigurationRepository notificationConfigurationRepository;
    private final EmailNotificationService emailNotificationService;
    private final NotificationRepository notificationRepository;
    private final KeywordsRepository keywordsRepository;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @EventListener
    public void onNewsFetched(NewsFetchedEvent newsFetchedEvent) {
        List<News> newsApiResponses = newsFetchedEvent.getFetchedNews();
        List<NotificationConfiguration> notificationConfigurations = notificationConfigurationRepository.findAllEnabled();
        log.info("Enabled configurations: {}", notificationConfigurations.size());

        Map<User, Map<NewsCategory, Set<String>>> userToCategoryKeywords = new HashMap<>();
        notificationConfigurations.forEach(notificationConfiguration -> {
            User user = notificationConfiguration.getUser();
            NewsCategory category = notificationConfiguration.getNewsCategory();
            Set<String> keywords = keywordsRepository
                    .findAllByUserAndNewsCategory(user.getId(), category.getCategoryName())
                    .stream()
                    .filter(Keyword::isActive)
                    .map(Keyword::getKeyword)
                    .collect(Collectors.toSet());

            userToCategoryKeywords
                    .computeIfAbsent(user, k -> new HashMap<>())
                    .put(category, keywords);
        });

        Map<User, List<News>> filteredNewsOfEachUser = new HashMap<>();
        userToCategoryKeywords.forEach((user, categoryKeywordsMap) -> {
            List<News> filteredNews = newsApiResponses
                    .stream()
                    .filter(news -> news.getCategories().stream().anyMatch(category -> {
                        Set<String> keywords = categoryKeywordsMap.getOrDefault(category, Set.of());
                        if (keywords.isEmpty()) {
                            return true;
                        }
                        return keywords.stream().anyMatch(keyword ->
                                news.getDescription().contains(keyword) || news.getHeadline().contains(keyword)
                        );
                    }))
                    .collect(Collectors.toList());

            if (!filteredNews.isEmpty()) {
                filteredNewsOfEachUser.put(user, filteredNews);
            }
        });

        sendEmailNotification(filteredNewsOfEachUser);
        saveNotifications(filteredNewsOfEachUser);
    }

    private void sendEmailNotification(Map<User, List<News>> filteredNewsOfEachUser) {
        List<EmailNotificationPayload> emailPayloads = new ArrayList<>();
        filteredNewsOfEachUser.forEach((user, newsToBeEmailed) -> {
            final String emailBody = buildEmailContent(newsToBeEmailed);
            EmailNotificationPayload emailNotificationPayload = new EmailNotificationPayload();
            emailNotificationPayload.setSenderEmailAddress(senderEmailAddress);
            emailNotificationPayload.setRecipientEmailAddress(user.getEmail());
            emailNotificationPayload.setSubject(EmailConstants.NEWS_EMAIL_SUBJECT);
            emailNotificationPayload.setBody(emailBody);
            emailPayloads.add(emailNotificationPayload);
        });

        log.info("Email notification payloads: {}", emailPayloads.size());
        emailNotificationService.sendEmailNotificationInBulk(emailPayloads);
    }

    private void saveNotifications(Map<User, List<News>> filteredNewsOfEachUser) {
        List<Notification> notifications = new ArrayList<>();
        filteredNewsOfEachUser.forEach((user, newsList) -> {
            newsList.forEach(news -> {
                Notification notification = new Notification();
                notification.setReceiver(user);
                notification.setContent("Headline: " + news.getHeadline().substring(news.getHeadline().length() / 2) + ", URL: " + news.getUrl());
                notification.setHasRead(false);
                notification.setNewsId(news.getId());
                notification.setNotificationType(NotificationType.EMAIL);
                notifications.add(notification);
            });
        });
        notificationRepository.saveAll(notifications);
    }

    private String buildEmailContent(List<News> newsList) {
        StringBuilder contentBuilder = new StringBuilder(EmailConstants.NEWS_EMAIL_CONTENT);
        final String joinDelimiter = ", ";
        Map<String, List<News>> categorizedNews = newsList
                .stream()
                .collect(
                        Collectors.groupingBy(news -> news.getCategories().stream()
                                .map(NewsCategory::getCategoryName)
                                .collect(Collectors.joining(joinDelimiter)))
                );
        categorizedNews.forEach((category, newsInCategory) -> {
            contentBuilder
                    .append(EmailConstants.NEWS_EMAIL_CONTENT_CATEGORY)
                    .append(category)
                    .append("\n");
            newsInCategory.forEach(news -> {
                contentBuilder
                        .append(" - ")
                        .append(news.getHeadline())
                        .append(EmailConstants.NEWS_EMAIL_CONTENT_TWO_LINE_BREAKS);
                contentBuilder
                        .append(EmailConstants.NEWS_EMAIL_CONTENT_URL)
                        .append(news.getUrl())
                        .append(EmailConstants.NEWS_EMAIL_CONTENT_TWO_LINE_BREAKS);
            });
            contentBuilder.append(EmailConstants.NEWS_EMAIL_CONTENT_THREE_LINE_BREAKS);
        });
        return contentBuilder.toString();
    }

}
