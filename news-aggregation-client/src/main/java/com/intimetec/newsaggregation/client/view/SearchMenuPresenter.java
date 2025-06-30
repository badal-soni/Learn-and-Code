package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;

import java.time.LocalDate;
import java.util.Scanner;

public class SearchMenuPresenter implements MenuPresenter {

    private final NewsService newsService;
    private final HeadlineMenu headlineMenu;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;
    private boolean isLoggedIn = false;

    public SearchMenuPresenter() {
        this.newsService = new NewsService();
        this.headlineMenu = new HeadlineMenu();
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
        for (String option : MenuChoices.SEARCH_NEWS_MENU) {
            consoleLogger.info(option);
        }

        int choice = inputReader.nextInt();
        if (choice == 1) {
            this.searchNews();
            this.headlineMenu.saveNews();
        } else if (choice == 2) {
            this.isLoggedIn = false;
            UserContextHolder.clearContext();
        }
    }

    private void searchNews() {
        SearchNewsRequest searchNewsRequest = new SearchNewsRequest();
        consoleLogger.info("Enter search text");
        String searchText = inputReader.next().trim();
        searchNewsRequest.setSearchQuery(searchText);

        consoleLogger.info("Do you want to give start date (y/n) ?");
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info("Enter start date (yyyy-mm-dd): ");
            String startDate = inputReader.next().trim();
            searchNewsRequest.setFrom(LocalDate.parse(startDate));
        }
        consoleLogger.info("Do you want to give end date (y/n) ?");
        choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info("Enter end date (yyyy-mm-dd): ");
            String endDate = inputReader.next().trim();
            searchNewsRequest.setFrom(LocalDate.parse(endDate));
        }

        searchNewsRequest.setIsAscendingOrder(null);
        consoleLogger.info("Do you want to sort by likes ? (1 for ascending, -1 for descending and 0 for no sorting");
        int sortOrder = inputReader.nextInt();
        if (sortOrder == 1) {
            searchNewsRequest.setIsAscendingOrder(true);
        } else if (sortOrder == -1) {
            searchNewsRequest.setIsAscendingOrder(false);
        }

        this.newsService.searchNews(searchNewsRequest);
    }


}
