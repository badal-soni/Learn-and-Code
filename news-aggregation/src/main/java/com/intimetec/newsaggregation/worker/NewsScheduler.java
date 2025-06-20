package com.intimetec.newsaggregation.worker;

import com.intimetec.newsaggregation.adapter.NewsFetcher;
import com.intimetec.newsaggregation.constant.NotificationType;
import com.intimetec.newsaggregation.dto.EmailNotificationPayload;
import com.intimetec.newsaggregation.entity.*;
import com.intimetec.newsaggregation.notification.EmailNotificationService;
import com.intimetec.newsaggregation.repository.NotificationConfigurationRepository;
import com.intimetec.newsaggregation.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class NewsScheduler {

    private final NotificationConfigurationRepository notificationConfigurationRepository;
    private final List<NewsFetcher> newsFetchers;
    private final EmailNotificationService emailNotificationService;
    private final NotificationRepository notificationRepository;

    private static final long THREE_HOURS_DELAY = 3 * 60 * 60 * 1000;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Scheduled(fixedDelay = THREE_HOURS_DELAY)
    @Async("asyncExecutor")
    public void fetchNewsFromExternalDataSource() {
        System.out.println("Fetching news from external data source...");
        List<News> newsApiResponses = fetchNews();

        if (Objects.isNull(newsApiResponses) || newsApiResponses.isEmpty()) {
            System.out.println("No news fetched from external data source.");
            return;
        }

        List<NotificationConfiguration> notificationConfigurations = notificationConfigurationRepository.findAllEnabled();
        for (NotificationConfiguration notificationConfiguration : notificationConfigurations) {
            Set<Keyword> keywords = notificationConfiguration.getUser().getKeywords();
            if (Objects.isNull(keywords) || keywords.isEmpty()) {
                sendNotificationsOfAllCategories(newsApiResponses, notificationConfiguration);
                continue;
            }
            List<News> matchedKeywordNews = new ArrayList<>();
            for (News news : newsApiResponses) {
                for (Keyword keyword : keywords) {
                    boolean isKeywordMatched = news.getHeadline().toLowerCase().contains(keyword.getKeyword().toLowerCase()) || news.getDescription().toLowerCase().contains(keyword.getKeyword().toLowerCase());
                    if (isKeywordMatched && keyword.isActive()) {
                        matchedKeywordNews.add(news);
                    }
                }
            }
            sendEmails(matchedKeywordNews, notificationConfiguration.getUser());
        }
    }

    private void sendNotificationsOfAllCategories(List<News> newsApiResponses, NotificationConfiguration notificationConfiguration) {
        System.out.println("here in method");
        final String category = notificationConfiguration.getNewsCategory().getCategoryName();
        List<News> filteredNews = new ArrayList<>();

        for (News news : newsApiResponses) {
            for (NewsCategory newsCategory : news.getCategories()) {
                if (newsCategory.getCategoryName().equalsIgnoreCase(category)) {
                    filteredNews.add(news);
                    break; // No need to check other categories for this news
                }
            }
        }
        sendEmails(filteredNews, notificationConfiguration.getUser());
    }

    private void sendEmails(List<News> filteredNews, User receiver) {
        List<EmailNotificationPayload> emailNotificationPayloads = new ArrayList<>();
        List<Notification> notifications = new ArrayList<>();
        for (News news : filteredNews) {
            EmailNotificationPayload emailNotificationPayload = new EmailNotificationPayload();
            emailNotificationPayload.setSenderEmailAddress(senderEmailAddress);
            emailNotificationPayload.setRecipientEmailAddress(receiver.getEmail());
            emailNotificationPayload.setSubject("Latest News: " + news.getHeadline());
            emailNotificationPayload.setBody("Check out the latest news: " + news.getUrl());
            emailNotificationPayloads.add(emailNotificationPayload);
            Notification notification = new Notification();
            notification.setReceiver(receiver);
            notification.setNotificationType(NotificationType.EMAIL);
            notification.setHasRead(false);
            notification.setContent(news.getUrl());
            notifications.add(notification);
        }
        emailNotificationService.sendEmailNotificationInBulk(emailNotificationPayloads);
        notificationRepository.saveAll(notifications);
    }

    private List<News> fetchNews() {
        for (NewsFetcher newsFetcher : newsFetchers) {
            List<News> newsApiResponses = newsFetcher.fetchNews();
            if (Objects.nonNull(newsApiResponses) && !newsApiResponses.isEmpty()) {
                return newsApiResponses;
            }
        }
        return null;
    }

}
