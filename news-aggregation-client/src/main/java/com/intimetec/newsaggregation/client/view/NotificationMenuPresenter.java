package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.Messages;
import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.displayer.KeywordDisplayer;
import com.intimetec.newsaggregation.client.displayer.NotificationConfigurationsDisplayer;
import com.intimetec.newsaggregation.client.displayer.NotificationDisplayer;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.service.NotificationConfigurationService;
import com.intimetec.newsaggregation.client.service.NotificationService;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.dto.response.NotificationConfigurationResponse;

import java.util.*;

public class NotificationMenuPresenter implements MenuPresenter {

    private final NotificationService notificationService;
    private final NotificationConfigurationService notificationConfigurationService;
    private final Scanner inputReader;
    private final ConsoleLogger consoleLogger;

    public NotificationMenuPresenter() {
        this.notificationService = new NotificationService();
        this.notificationConfigurationService = new NotificationConfigurationService();
        this.inputReader = new Scanner(System.in);
        this.consoleLogger = new ConsoleLogger();
    }

    @Override
    public void showMenu() {
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
        var notifications = this.notificationService.getAllNotifications();
        NotificationDisplayer.displayNotifications(notifications);
    }

    private void configureNotificationSettings() {
        AllNotificationConfigurations allNotificationConfigurations = this.notificationConfigurationService.getAllNotificationConfigurations();
        NotificationConfigurationsDisplayer.displayConfigurations(allNotificationConfigurations);

        List<NotificationConfigurationResponse> notificationConfigurations = allNotificationConfigurations.getNewsCategories();
        final Map<Integer, NotificationConfigurationResponse> preferencesMap = buildPreferencesMap(notificationConfigurations);

        preferencesMap.forEach((position, notificationConfigurationResponse) -> {
            consoleLogger.info("Enter " + position + ". to toggle enable/disable status of " + notificationConfigurationResponse.getCategoryName() + ": Enabled: " + notificationConfigurationResponse.isEnabled());
        });
        consoleLogger.info("Enter " + (preferencesMap.size() + 1) + " to skip");

        int option = inputReader.nextInt();
        if (option < 1 || option > preferencesMap.size() + 1) {
            consoleLogger.error(Messages.INVALID_OPTION);
            return;
        }
        if (option == preferencesMap.size() + 1) {
            consoleLogger.info(Messages.SKIP_NOTIFICATION_PREFERENCES);
            return;
        }
        UpdateNotificationPreferencesRequest.NotificationPreference notificationPreference = new UpdateNotificationPreferencesRequest.NotificationPreference(
                preferencesMap.get(option).getCategoryName(),
                !preferencesMap.get(option).isEnabled()
        );
        UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest = new UpdateNotificationPreferencesRequest();
        updateNotificationPreferencesRequest.setPreferences(List.of(notificationPreference));
        this.notificationConfigurationService.updateNotificationPreferences(updateNotificationPreferencesRequest);
    }

    private Map<Integer, NotificationConfigurationResponse> buildPreferencesMap(List<NotificationConfigurationResponse> notificationConfigurations) {
        Map<Integer, NotificationConfigurationResponse> preferencesMap = new TreeMap<>();
        for (int index = 0; index < notificationConfigurations.size(); index++) {
            final int position = index + 1;
            preferencesMap.put(position, notificationConfigurations.get(index));
        }
        return preferencesMap;
    }

    private void viewKeywords() {
        displayKeywords();
        consoleLogger.info(Messages.TOGGLE_KEYWORD_ACTIVE_STATUS);
        char choice = inputReader.next().toLowerCase().charAt(0);
        if (Character.toLowerCase(choice) == 'y') {
            toggleKeywordActiveStatus();
        }
    }

    private void toggleKeywordActiveStatus() {
        displayKeywords();
        consoleLogger.info(Messages.ENTER_KEYWORD_ID);
        Long keywordId = inputReader.nextLong();
        this.notificationConfigurationService.toggleKeywordActiveStatus(keywordId);
    }

    private void displayKeywords() {
        List<KeywordResponse> allKeywordsOfUser = this.notificationConfigurationService.getAllKeywordsOfUser();
        KeywordDisplayer.displayKeywords(allKeywordsOfUser);
    }

    private void addKeyword() {
        consoleLogger.info(Messages.ENTER_CATEGORY_NAME);
        String newsCategory = inputReader.next();

        consoleLogger.info(Messages.ENTER_KEYWORD);
        String keyword = inputReader.next();

        CreateKeywordRequest createKeywordRequest = new CreateKeywordRequest();
        createKeywordRequest.setKeyword(keyword);
        createKeywordRequest.setParentCategory(newsCategory);

        this.notificationConfigurationService.addKeyword(createKeywordRequest);
    }

}
