package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.service.DashboardService;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;

import java.util.List;
import java.util.Scanner;

public class SavedArticleMenu implements MenuPresenter {

    private final NewsService newsService;
    private final DashboardService dashboardService;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;
    private boolean isLoggedIn = false;

    public SavedArticleMenu() {
        this.newsService = new NewsService();
        this.dashboardService = new DashboardService();
        this.consoleLogger = new ConsoleLogger();
        this.inputReader = new Scanner(System.in);
    }

    @Override
    public void showMenu() {
        this.isLoggedIn = true;
        while (this.isLoggedIn) {
            this.renderOptions();
        }
    }

    private void renderOptions() {
        this.showSavedNews();
        for (String option: MenuChoices.SAVED_ARTICLES_MENU) {
            consoleLogger.info(option);
        }

        int choice = inputReader.nextInt();
        if (choice == 1) {
            this.unSaveNews();
        } else if (choice == 3) {
            this.isLoggedIn = false;
            UserContextHolder.clearContext();
        }
    }

    private void showSavedNews() {
        List<SavedNewsResponse> savedNewsResponseList = this.dashboardService.getSavedNews();
        savedNewsResponseList.forEach(consoleLogger::info);
    }

    private void unSaveNews() {
        consoleLogger.info("Enter the news id: ");
        Long newsId = inputReader.nextLong();
        this.newsService.unSaveNews(newsId);
    }

}
