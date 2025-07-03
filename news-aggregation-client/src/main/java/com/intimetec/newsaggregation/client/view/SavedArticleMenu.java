package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.constant.Messages;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.displayer.SavedNewsDisplayer;
import com.intimetec.newsaggregation.client.service.DashboardService;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;

import java.util.List;
import java.util.Scanner;

public class SavedArticleMenu implements MenuPresenter {

    private final NewsService newsService;
    private final DashboardService dashboardService;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;

    public SavedArticleMenu() {
        this.newsService = new NewsService();
        this.dashboardService = new DashboardService();
        this.consoleLogger = new ConsoleLogger();
        this.inputReader = new Scanner(System.in);
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
        boolean hasNews = this.showSavedNews();
        if (!hasNews) {
            consoleLogger.info(Messages.NOT_SAVED_ANY_NEWS);
            return true;
        }
        for (String option: MenuChoices.SAVED_ARTICLES_MENU) {
            consoleLogger.info(option);
        }

        int choice = inputReader.nextInt();
        if (choice == 1) {
            this.unSaveNews();
        } else if (choice == 3) {
            UserContextHolder.clearContext();
            return false;
        }
        return true;
    }

    private boolean showSavedNews() {
        List<SavedNewsResponse> savedNewsResponseList = this.dashboardService.getSavedNews();
        if (!savedNewsResponseList.isEmpty()) {
            SavedNewsDisplayer.displaySavedNews(savedNewsResponseList);
        }
        return !savedNewsResponseList.isEmpty();
    }

    private void unSaveNews() {
        consoleLogger.info(Messages.ENTER_NEWS_ID);
        Long newsId = inputReader.nextLong();
        this.newsService.unSaveNews(newsId);
    }

}
