package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.InputPrompts;
import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.client.dto.request.CreateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.request.UpdateExternalServerRequest;
import com.intimetec.newsaggregation.client.service.ExternalServerDetailService;
import com.intimetec.newsaggregation.client.service.NewsCategoryService;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class AdminMenuPresenter implements MenuPresenter {

    private final Scanner scanner;
    private final ConsoleLogger consoleLogger;
    private final ExternalServerDetailService externalServerDetailService;
    private final NewsCategoryService newsCategoryService;
    private final NewsService newsService;

    public AdminMenuPresenter() {
        this.scanner = new Scanner(System.in);
        this.consoleLogger = new ConsoleLogger();
        this.externalServerDetailService = new ExternalServerDetailService();
        this.newsCategoryService = new NewsCategoryService();
        this.newsService = new NewsService();
    }

    @Override
    public void showMenu() {
        for (String welcomeMenuChoice : MenuChoices.ADMIN_MENU_CHOICES) {
            consoleLogger.info(welcomeMenuChoice);
        }

        int choice = scanner.nextInt();
        if (choice == 1) {
            this.registerExternalServer();
        }
        if (choice == 2) {
            this.viewExternalServers();
        }
        if (choice == 3) {
            this.viewApiKey();
        }
        if (choice == 4) {
            this.updateExternalServer();
        }
        if (choice == 5) {
            this.createCategory();
        }
        if (choice == 6) {
            this.viewAllReportedNews();
        }
        if (choice == 7) {
            this.hideNews();
        }
        if (choice == 8) {
            this.unhideNews();
        }
        if (choice == 9) {
            this.hideCategory();
        }
        if (choice == 10) {
            this.unhideCategory();;
        }
        if (choice == 11) {
            this.viewAllHiddenNews();
        }
        if (choice == 12) {
            this.viewAllHiddenCategories();
        }
        if (choice == 13) {
            UserContextHolder.clearContext();
        }
    }

    private void registerExternalServer() {
        CreateExternalServerRequest createExternalServerRequest = new CreateExternalServerRequest();
        consoleLogger.info(InputPrompts.ENTER_SERVER_NAME);
        createExternalServerRequest.setServerName(scanner.next());

        consoleLogger.info(InputPrompts.ENTER_API_URL);
        createExternalServerRequest.setApiUrl(scanner.next());

        consoleLogger.info(InputPrompts.ENTER_API_KEY);
        createExternalServerRequest.setApiKey(scanner.next());

        this.externalServerDetailService.registerExternalServer(createExternalServerRequest);
    }

    private void viewExternalServers() {
        this.externalServerDetailService.viewExternalServers();
    }

    private void viewApiKey() {
        this.externalServerDetailService.viewApiKey();
    }

    private void updateExternalServer() {
        UpdateExternalServerRequest updateExternalServerRequest = new UpdateExternalServerRequest();

        consoleLogger.info(InputPrompts.ENTER_EXTERNAL_SERVER_ID);
        updateExternalServerRequest.setServerId(scanner.nextInt());

        consoleLogger.info(InputPrompts.ENTER_EXTERNAL_SERVER_NAME);
        updateExternalServerRequest.setApiKeyToUpdate(scanner.next());

        consoleLogger.info(InputPrompts.ENABLE_SERVER_STATUS);
        consoleLogger.info(InputPrompts.DISABLE_SERVER_STATUS);
        boolean enableStatus = true;
        int choice = scanner.nextInt();
        if (choice == 2) {
            enableStatus = false;
        }
        updateExternalServerRequest.setEnabled(enableStatus);
        this.externalServerDetailService.updateExternalServer(updateExternalServerRequest);
    }

    private void createCategory() {
        CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest();

        consoleLogger.info(InputPrompts.ENTER_CATEGORY_NAME);
        createCategoryRequest.setCategoryName(scanner.next());

        this.newsCategoryService.addCategory(createCategoryRequest);
    }

    private void viewAllReportedNews() {
        List<ReportedNewsResponse> reportedNewsList = this.newsService.getAllReportedNews();
        for (ReportedNewsResponse reportedNews : reportedNewsList) {
            consoleLogger.info(reportedNews);
        }
        consoleLogger.info("\n");
    }

    private void hideNews() {
        NewsIds newIds = inputNewsIds();
        this.newsService.hideNews(newIds);
    }

    private void unhideNews() {
        NewsIds newsIds = inputNewsIds();
        this.newsService.unhideNews(newsIds);
    }

    private NewsIds inputNewsIds() {
        List<Long> newsIdList = new ArrayList<>();
        consoleLogger.info(InputPrompts.ENTER_NEWS_ID);
        Long newsId = scanner.nextLong();
        do {
            newsIdList.add(newsId);
            consoleLogger.info("Enter -1 to stop");
            newsId = scanner.nextLong();
        } while (newsId != -1);
        NewsIds newsIds = new NewsIds();
        newsIds.setNewsIds(newsIdList);
        return newsIds;
    }

    public void hideCategory() {
        List<String> newsCategoriesToHide = inputNewsCategories();
        this.newsCategoryService.hideNewsCategories(newsCategoriesToHide);
    }

    public void unhideCategory() {
        List<String> newsCategoriesToUnhide = inputNewsCategories();
        this.newsCategoryService.hideNewsCategories(newsCategoriesToUnhide);
    }

    private List<String> inputNewsCategories() {
        List<String> categories = new ArrayList<>();
        consoleLogger.info("Enter news categories");
        String category = scanner.next().trim();
        do {
            categories.add(category);
            consoleLogger.info("Enter -1 to stop");
            category = scanner.next().trim();
        } while (!category.equals("-1"));
        return categories;
    }

    public void viewAllHiddenNews() {
        List<NewsResponse> hiddenNews = this.newsService.getAllHiddenNews();
        for (NewsResponse newsResponse: hiddenNews) {
            consoleLogger.info(newsResponse);
        }
    }

    public void viewAllHiddenCategories() {
        Optional<NewsCategoryResponse> newsCategoryResponse = this.newsCategoryService.getAllHiddenNewsCategories();
        newsCategoryResponse.ifPresent(category -> {
            final List<NewsCategoryResponse.NewsCategoryDetail> newsCategoryDetails = category.getNewsCategoryDetails();
            newsCategoryDetails.forEach(consoleLogger::info);
        });
    }

}
