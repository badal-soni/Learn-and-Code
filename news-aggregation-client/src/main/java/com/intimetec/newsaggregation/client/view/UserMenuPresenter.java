package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.InputPrompts;
import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.constant.UserDashboardKeys;
import com.intimetec.newsaggregation.client.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.client.factory.MenuFactory;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.service.NotificationConfigurationService;
import com.intimetec.newsaggregation.client.service.NotificationService;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.NewsConfigurationResponse;

import java.time.LocalDate;
import java.util.*;

public class UserMenuPresenter implements MenuPresenter {

    private final Scanner scanner;
    private final ConsoleLogger consoleLogger;
    private final NewsService newsService;
    private final NotificationService notificationService;
    private final NotificationConfigurationService notificationConfigurationService;

    public UserMenuPresenter() {
        this.consoleLogger = new ConsoleLogger();
        this.scanner = new Scanner(System.in);
        this.newsService = new NewsService();
        this.notificationService = new NotificationService();
        this.notificationConfigurationService = new NotificationConfigurationService();
    }

    @Override
    public void showMenu() {
        for (String welcomeMenuChoice : MenuChoices.USER_MENU_CHOICES) {
            consoleLogger.info(welcomeMenuChoice);
        }
        int choice = scanner.nextInt();
        if (choice == 1) {
            MenuPresenter headlineMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.HEADLINES);
            headlineMenu.showMenu();
        } else if (choice == 2) {
            MenuPresenter savedArticlesMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.SAVED_ARTICLES);
            savedArticlesMenu.showMenu();
        } else if (choice == 3) {
            MenuPresenter searchNewsMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.SEARCH);
            searchNewsMenu.showMenu();
        } else if (choice == 4) {
            MenuPresenter notificationMenu = MenuFactory.getMenuPresenter(UserDashboardKeys.NOTIFICATIONS);
            notificationMenu.showMenu();
        }

//        if (choice == 1) {
//            this.viewHeadlines();
//        } else if (choice == 2) {
//            saveNews();
//        } else if (choice == 3) {
//            searchNewsArticle();
//        } else if (choice == 4) {
//            likeNews();
//        } else if (choice == 5) {
//            configureNotificationSettings();
//        } else if (choice == 6) {
//            viewNotifications();
//        }
    }

    private void saveNews() {
        consoleLogger.info(InputPrompts.ENTER_NEWS_ID);
        long newsId = scanner.nextLong();
        this.newsService.saveNews(newsId);
    }

    private void likeNews() {
        consoleLogger.info(InputPrompts.ENTER_NEWS_ID);
        long newsId = scanner.nextLong();

        consoleLogger.info(InputPrompts.LIKE_NEWS);
        char answer = scanner.next().charAt(0);

        if (Character.toLowerCase(answer) == 'y') {
            this.newsService.toggleNewsLike(newsId);
        }
    }

    private void viewNotifications() {
        List<NotificationResponse> notifications = this.notificationService.getAllNotifications();
        notifications.forEach(notification -> {
            consoleLogger.info("Content: " + notification.getContent());
        });
    }

    private void viewHeadlines() {
        consoleLogger.info(InputPrompts.VIEW_TODAY_NEWS);
        consoleLogger.info(InputPrompts.VIEW_DATE_RANGE);

        int choice = scanner.nextInt();
        if (choice == 1) {
            List<NewsResponse> todaysNews = this.newsService.viewTodaysNews();
            todaysNews.forEach(newsResponse -> {
                consoleLogger.info(newsResponse.toString());
            });
        } else if (choice == 2) {
            consoleLogger.info("Enter start date (yyyy-MM-dd): ");
            String startDate = scanner.next();

            consoleLogger.info("Enter end date (yyyy-MM-dd): ");
            String endDate = scanner.next();

//            Map<Integer, String> categoryMap = getCategoryChoice(this.newsCategoryService.getAllCategories());
//            categoryMap.forEach((categoryId, categoryName) -> {
//                consoleLogger.info(categoryId + ". " + categoryName);
//            });
//
//            consoleLogger.info("Enter the choice: ");
//            int categoryChoice = scanner.nextInt();
//
//            List<NewsResponse> newsResponses = this.newsService.viewUnderCategoryBetweenDates(
//                    categoryMap.get(categoryChoice),
//                    LocalDate.parse(startDate),
//                    LocalDate.parse(endDate)
//            );
//            newsResponses.forEach(consoleLogger::info);
        }
    }

    private Map<Integer, String> getCategoryChoice(List<String> categories) {
        Map<Integer, String> categoryMap = new TreeMap<>();
        categoryMap.put(1, Constants.ALL_CATEGORIES);
        for (int i = 2; i < categories.size(); i++) {
            categoryMap.put(i, categories.get(i));
        }
        return categoryMap;
    }

    private void searchNewsArticle() {
        consoleLogger.info("Enter search query: ");
        String searchQuery = scanner.next();

        consoleLogger.info("Enter start date (yyyy-MM-dd): ");
        String startDate = scanner.next();

        consoleLogger.info("Enter end date (yyyy-MM-dd): ");
        String endDate = scanner.next();

        consoleLogger.info("Press 1 to sort by likes, 2 to sort by dislikes");
        int sortChoice = scanner.nextInt();

        SearchNewsRequest searchNewsRequest = new SearchNewsRequest();
        searchNewsRequest.setSearchQuery(searchQuery);
        searchNewsRequest.setFromDate(LocalDate.parse(startDate));
        searchNewsRequest.setToDate(LocalDate.parse(endDate));
        searchNewsRequest.setPageIndex(Constants.PAGE_INDEX);
        searchNewsRequest.setPageSize(Constants.PAGE_SIZE);
        if (sortChoice == 1) {
            searchNewsRequest.setSortBy(Constants.LIKES);
        } else if (sortChoice == 2) {
            searchNewsRequest.setSortBy(Constants.DISLIKES);
        }
        consoleLogger.info("Dou you want to sort in ascending order? (y/n)");
        char answer = scanner.next().charAt(0);
        if (Character.toLowerCase(answer) == 'y') {
            searchNewsRequest.setAscendingOrder(true);
        } else {
            searchNewsRequest.setAscendingOrder(false);
        }

//        List<com.intimetec.newsaggregation.dto.response.NewsResponse> newsResponses = this.newsService.searchNews(s);
//        newsResponses.forEach(consoleLogger::info);
    }

    private void configureNotificationSettings() {
        AllNotificationConfigurations allNotificationConfigurations = this.notificationConfigurationService.getAllNotificationConfigurations();
        consoleLogger.info(allNotificationConfigurations.toString());

        List<UpdateNotificationPreferencesRequest.NotificationPreference> notificationPreferences = new ArrayList<>();
        List<NewsConfigurationResponse> newsCategories = allNotificationConfigurations.getNewsCategories();
        for (NewsConfigurationResponse newsCategory : newsCategories) {
            consoleLogger.info("Do you want to enable notifications for " + newsCategory.getCategoryName() + "? (y/n)");
            char enable = scanner.next().charAt(0);
            boolean shouldEnable = Character.toLowerCase(enable) == 'y';
            notificationPreferences.add(new UpdateNotificationPreferencesRequest.NotificationPreference(
                    newsCategory.getCategoryName(), shouldEnable));
        }
        UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest = new UpdateNotificationPreferencesRequest();
        updateNotificationPreferencesRequest.setPreferences(notificationPreferences);
        this.notificationConfigurationService.updateNotificationPreferences(updateNotificationPreferencesRequest);
    }

}
