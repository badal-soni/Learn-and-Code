package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.client.service.NotificationConfigurationService;
import com.intimetec.newsaggregation.client.service.NotificationService;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.dto.response.NewsConfigurationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NotificationMenuPresenter implements MenuPresenter {

    private final NotificationService notificationService;
    private final NotificationConfigurationService notificationConfigurationService;
    private final Scanner inputReader;
    private final ConsoleLogger consoleLogger;
    private boolean isLoggedIn = false;

    public NotificationMenuPresenter() {
        this.notificationService = new NotificationService();
        this.notificationConfigurationService = new NotificationConfigurationService();
        this.inputReader = new Scanner(System.in);
        this.consoleLogger = new ConsoleLogger();
    }

    @Override
    public void showMenu() {
        this.isLoggedIn = true;
        while (UserContextHolder.isLoggedIn) {
            if (this.renderOptions()) {
                return;
            }
        }
    }

    private boolean renderOptions() {
        for (String option : MenuChoices.NOTIFICATION_MENU) {
            consoleLogger.info(option);
        }

        int choice = inputReader.nextInt();
        if (choice == 1) {
            this.viewNotifications();
        } else if (choice == 2) {
            this.configureNotificationSettings();
        } else if (choice == 3) {
            this.viewKeywords();
        } else if (choice == 4) {
            this.addKeyword();
        } else if (choice == 5) {
            this.toggleKeywordActiveStatus();
        } else if (choice == 6) {
            UserContextHolder.clearContext();
            return false;
        }
        return true;
    }

    private void viewNotifications() {
        List<NotificationResponse> notifications = this.notificationService.getAllNotifications();
        notifications.forEach(notification -> {
            consoleLogger.info("Content: " + notification.getContent());
        });
    }

    private void configureNotificationSettings() {
        AllNotificationConfigurations allNotificationConfigurations = this.notificationConfigurationService.getAllNotificationConfigurations();
        consoleLogger.info(allNotificationConfigurations.toString());

        List<UpdateNotificationPreferencesRequest.NotificationPreference> notificationPreferences = new ArrayList<>();
        List<NewsConfigurationResponse> newsCategories = allNotificationConfigurations.getNewsCategories();
        for (NewsConfigurationResponse newsCategory : newsCategories) {
            consoleLogger.info("Enter 1 to enable, 2 to disable, 3 to skip");
            int choice = inputReader.nextInt();
            if (choice == 3) {
                continue;
            }
            boolean shouldEnable = choice == 1;
            notificationPreferences.add(
                    new UpdateNotificationPreferencesRequest.NotificationPreference(
                            newsCategory.getCategoryName(), shouldEnable)
            );
        }
        UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest = new UpdateNotificationPreferencesRequest();
        updateNotificationPreferencesRequest.setPreferences(notificationPreferences);
        this.notificationConfigurationService.updateNotificationPreferences(updateNotificationPreferencesRequest);
    }

    private void viewKeywords() {
        List<KeywordResponse> allKeywordsOfUser = this.notificationConfigurationService.getAllKeywordsOfUser();;
        allKeywordsOfUser.forEach(consoleLogger::info);
    }

    private void toggleKeywordActiveStatus() {
        consoleLogger.info("Enter the keyword id: ");
        Long keywordId = inputReader.nextLong();
        this.notificationConfigurationService.toggleKeywordActiveStatus(keywordId);
    }

    private void addKeyword() {
        consoleLogger.info("Enter the category under which you want to create the keyword");
        String newsCategory = inputReader.next();

        consoleLogger.info("Enter the keyword");
        String keyword = inputReader.next();

        CreateKeywordRequest createKeywordRequest = new CreateKeywordRequest();
        createKeywordRequest.setKeyword(keyword);
        createKeywordRequest.setParentCategory(newsCategory);

        this.notificationConfigurationService.addKeyword(createKeywordRequest);
    }

}
