package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.constant.Messages;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;

import java.time.LocalDate;
import java.util.Scanner;

public class SearchMenuPresenter implements MenuPresenter {

    private final NewsService newsService;
    private final HeadlineMenu headlineMenu;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;

    public SearchMenuPresenter() {
        this.newsService = new NewsService();
        this.headlineMenu = new HeadlineMenu();
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
        for (String option : MenuChoices.SEARCH_NEWS_MENU) {
            consoleLogger.info(option);
        }

        int choice = inputReader.nextInt();
        if (choice == 1) {
            this.searchNews();
            this.headlineMenu.saveNews();
        } else if (choice == 2) {
            UserContextHolder.clearContext();
            return false;
        }
        return true;
    }

    private void searchNews() {
        SearchNewsRequest searchNewsRequest = new SearchNewsRequest();
        consoleLogger.info(Messages.ENTER_SEARCH_TEXT);
        String searchText = inputReader.next().trim();
        searchNewsRequest.setSearchQuery(searchText);

        consoleLogger.info(Messages.WANT_TO_GIVE_START_DATE);
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info(Messages.ENTER_START_DATE);
            String startDate = inputReader.next().trim();
            searchNewsRequest.setFrom(LocalDate.parse(startDate));
        }
        consoleLogger.info(Messages.WANT_TO_GIVE_END_DATE);
        choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info(Messages.ENTER_END_DATE);
            String endDate = inputReader.next().trim();
            searchNewsRequest.setFrom(LocalDate.parse(endDate));
        }

        searchNewsRequest.setIsAscendingOrder(null);
        consoleLogger.info(Messages.SORT_BY_LIKES);
        int sortOrder = inputReader.nextInt();
        if (sortOrder == 1) {
            searchNewsRequest.setIsAscendingOrder(true);
        } else if (sortOrder == -1) {
            searchNewsRequest.setIsAscendingOrder(false);
        }

        var newsResponses = this.newsService.searchNews(searchNewsRequest);
        for (NewsResponse newsResponse: newsResponses) {
            consoleLogger.info(newsResponse);
        }
    }

}
