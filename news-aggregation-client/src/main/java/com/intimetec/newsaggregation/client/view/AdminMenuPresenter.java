package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.Messages;
import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.displayer.ExternalServerDisplayer;
import com.intimetec.newsaggregation.client.displayer.NewsCategoryDisplayer;
import com.intimetec.newsaggregation.client.displayer.NewsDisplayer;
import com.intimetec.newsaggregation.client.displayer.ReportedNewsDisplayer;
import com.intimetec.newsaggregation.client.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.client.dto.request.CreateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.request.UpdateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.service.ExternalServerDetailService;
import com.intimetec.newsaggregation.client.service.NewsCategoryService;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;
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
            this.unHideNews();
        }
        if (choice == 9) {
            this.hideCategory();
        }
        if (choice == 10) {
            this.unHideCategory();;
        }
        if (choice == 11) {
            this.viewAllHiddenNews();
        }
        if (choice == 12) {
            this.viewAllHiddenNewsCategories();
        }
        if (choice == 13) {
            this.hideNewsByKeywords();
        }
        if (choice == 14) {
            UserContextHolder.clearContext();
        }
    }

    private void registerExternalServer() {
        scanner.nextLine();

        CreateExternalServerRequest createExternalServerRequest = new CreateExternalServerRequest();
        consoleLogger.info(Messages.ENTER_SERVER_NAME);
        String serverName = scanner.nextLine();
        createExternalServerRequest.setServerName(serverName);

        consoleLogger.info(Messages.ENTER_API_URL);
        createExternalServerRequest.setApiUrl(scanner.next());

        consoleLogger.info(Messages.ENTER_API_KEY);
        createExternalServerRequest.setApiKey(scanner.next());

        this.externalServerDetailService.registerExternalServer(createExternalServerRequest);
    }

    private void viewExternalServers() {
        List<ExternalServerStatusResponse> externalServers = this.externalServerDetailService.getExternalServerStatus();
        ExternalServerDisplayer.displayServerDetails(externalServers);
    }

    private void viewApiKey() {
        List<ExternalServerStatusResponse> externalServerDetail = this.externalServerDetailService.getExternalServerApiKeyDetails();
        ExternalServerDisplayer.displayServerApiKeys(externalServerDetail);
    }

    private void updateExternalServer() {
        ExternalServerDisplayer.displayCompleteServerDetails(this.externalServerDetailService.getExternalServerStatus());
        UpdateExternalServerRequest updateExternalServerRequest = new UpdateExternalServerRequest();

        consoleLogger.info(Messages.ENTER_EXTERNAL_SERVER_ID);
        updateExternalServerRequest.setServerId(scanner.nextInt());

        consoleLogger.info(Messages.ENTER_EXTERNAL_SERVER_NAME);
        updateExternalServerRequest.setApiKeyToUpdate(scanner.next());

        consoleLogger.info(Messages.ENABLE_SERVER_STATUS);
        consoleLogger.info(Messages.DISABLE_SERVER_STATUS);
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

        consoleLogger.info(Messages.ENTER_CATEGORY_NAME);
        createCategoryRequest.setCategoryName(scanner.next());

        this.newsCategoryService.addCategory(createCategoryRequest);
    }

    private void viewAllReportedNews() {
        List<ReportedNewsResponse> reportedNewsList = this.newsService.getAllReportedNews();
        ReportedNewsDisplayer.displayReportedNews(reportedNewsList);
    }

    private void hideNews() {
        NewsIds newIds = inputNewsIds();
        this.newsService.hideNews(newIds);
    }

    private void unHideNews() {
        NewsIds newsIds = inputNewsIds();
        this.newsService.unHideNews(newsIds);
    }

    private NewsIds inputNewsIds() {
        List<Long> newsIdList = new ArrayList<>();
        consoleLogger.info(Messages.ENTER_NEWS_ID);
        Long newsId = scanner.nextLong();
        do {
            newsIdList.add(newsId);
            consoleLogger.info(Messages.ENTER_TO_STOP);
            newsId = scanner.nextLong();
        } while (newsId != -1);
        NewsIds newsIds = new NewsIds();
        newsIds.setNewsIds(newsIdList);
        return newsIds;
    }

    public void hideCategory() {
        viewAllUnHiddenNewsCategories();
        List<String> newsCategoriesToHide = inputNewsCategories();
        this.newsCategoryService.hideNewsCategories(newsCategoriesToHide);
    }

    public void unHideCategory() {
        viewAllHiddenNewsCategories();
        List<String> newsCategoriesToUnhide = inputNewsCategories();
        this.newsCategoryService.unHideNewsCategories(newsCategoriesToUnhide);
    }

    private List<String> inputNewsCategories() {
        List<String> categories = new ArrayList<>();
        consoleLogger.info(Messages.ENTER_NEWS_CATEGORIES);
        String category = scanner.next().trim();
        do {
            categories.add(category);
            consoleLogger.info(Messages.ENTER_TO_STOP);
            category = scanner.next().trim();
        } while (!category.equals("-1"));
        return categories;
    }

    public void viewAllHiddenNews() {
        List<NewsResponse> hiddenNews = this.newsService.getAllHiddenNews();
        NewsDisplayer.displayNews(hiddenNews);
    }

    public void viewAllHiddenNewsCategories() {
        Optional<NewsCategoryResponse> newsCategoryResponse = this.newsCategoryService.getAllHiddenNewsCategories();
        newsCategoryResponse.ifPresent(category -> {
            NewsCategoryDisplayer.displayNewsCategories(category.getNewsCategoryDetails());
        });
    }

    public void viewAllUnHiddenNewsCategories() {
        Optional<NewsCategoryResponse> newsCategoryResponse = this.newsCategoryService.getAllUnHiddenNewsCategories();
        newsCategoryResponse.ifPresent(category -> {
            NewsCategoryDisplayer.displayNewsCategories(category.getNewsCategoryDetails());
        });
    }

    public void hideNewsByKeywords() {
        List<String> keywordList = new ArrayList<>();
        char choice = 'n';
        do {
            consoleLogger.info(Messages.ENTER_KEYWORD);
            String keyword = scanner.next().trim();
            keywordList.add(keyword);

            consoleLogger.info(Messages.ADD_MORE);
            choice = scanner.next().charAt(0);
        } while (choice == 'y');

        Keywords keywords = new Keywords();
        keywords.setKeywords(keywordList);
        this.newsService.hideNewsByKeywords(keywords);
    }

}
