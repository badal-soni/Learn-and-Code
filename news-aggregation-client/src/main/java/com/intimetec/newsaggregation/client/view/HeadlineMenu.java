package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.Messages;
import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.service.NewsCategoryService;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.displayer.NewsDisplayer;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;

import java.time.LocalDate;
import java.util.*;

public class HeadlineMenu implements MenuPresenter {

    private final NewsService newsService;
    private final NewsCategoryService newsCategoryService;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;

    public HeadlineMenu() {
        this.newsService = new NewsService();
        this.newsCategoryService = new NewsCategoryService();
        this.consoleLogger = new ConsoleLogger();
        this.inputReader = new Scanner(System.in);
    }

    @Override
    public void showMenu() {
        while (UserContextHolder.isLoggedIn) {
            if (this.renderHeadlineView()) {
                return;
            }
        }
    }

    private boolean renderHeadlineView() {
        for (String option: MenuChoices.HEADLINES_MENU) {
            consoleLogger.info(option);
        }
        int choice = inputReader.nextInt();
        if (choice == 1) {
            this.viewTodaysHeadline();
        } else if (choice == 2) {
            this.viewHeadlinedInDateRange();
        } else if (choice == 3) {
            return false;
        }
        return true;
    }

    private void viewTodaysHeadline() {
        List<NewsResponse> todaysNews = this.newsService.viewTodaysNews();
        NewsDisplayer.displayNews(todaysNews);
        this.viewNewsById();
        this.reportNews();
        this.likeNews();
    }

    private void viewHeadlinedInDateRange() {
        consoleLogger.info(Messages.ENTER_START_DATE);
        String startDate = inputReader.next();

        consoleLogger.info(Messages.ENTER_END_DATE);
        String endDate = inputReader.next();

        Optional<NewsCategoryResponse> newsCategoryResponses = this.newsCategoryService.getAllCategories();
        Map<Integer, String> categoryMap = getCategoryChoice(newsCategoryResponses.get());
        categoryMap.forEach((categoryId, categoryName) -> consoleLogger.info(categoryId + ". " + categoryName));

        consoleLogger.info(Messages.ENTER_CHOICE);
        int categoryChoice = inputReader.nextInt();

        List<NewsResponse> newsResponses = this.newsService.getAllNewsUnderCategoryBetweenDates(
                categoryMap.get(categoryChoice),
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        NewsDisplayer.displayNews(newsResponses);
        this.viewNewsById();
        this.reportNews();
        this.likeNews();
        this.saveNews();;
    }

    private Map<Integer, String> getCategoryChoice(NewsCategoryResponse newsCategoryResponse) {
        Map<Integer, String> categoryMap = new TreeMap<>();
        categoryMap.put(1, Constants.ALL_CATEGORIES);

        final List<NewsCategoryResponse.NewsCategoryDetail> newsCategoryDetails = newsCategoryResponse.getNewsCategoryDetails();
        for (int i = 2; i < newsCategoryDetails.size(); i++) {
            categoryMap.put(i, newsCategoryDetails.get(i).categoryName());
        }
        return categoryMap;
    }

    private void reportNews() {
        consoleLogger.info(Messages.REPORT_NEWS);
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info(Messages.ENTER_NEWS_ID);
            Long newsId = inputReader.nextLong();

            consoleLogger.info(Messages.ENTER_REPORT_REASON);
            String reportReason = inputReader.next();

            ReportNewsArticleRequest reportNewsArticleRequest = new ReportNewsArticleRequest();
            reportNewsArticleRequest.setReportReason(reportReason);

            this.newsService.reportNews(newsId, reportNewsArticleRequest);

            consoleLogger.info(Messages.ADD_MORE);
            choice = inputReader.next().charAt(0);
        }
    }

    private void likeNews() {
        consoleLogger.info(Messages.LIKE_NEWS);
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info(Messages.ENTER_NEWS_CATEGORIES);
            long newsId = inputReader.nextLong();
            this.newsService.toggleNewsLike(newsId);

            consoleLogger.info(Messages.ADD_MORE);
            choice = inputReader.next().charAt(0);
        }
    }

    public void saveNews() {
        consoleLogger.info(Messages.SAVE_NEWS);
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info(Messages.ENTER_NEWS_ID);
            Long newsId = inputReader.nextLong();
            this.newsService.saveNews(newsId);

            consoleLogger.info(Messages.ADD_MORE);
            choice = inputReader.next().charAt(0);
        }
    }

    private void viewNewsById() {
        consoleLogger.info(Messages.VIEW_NEWS_BY_ID);
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info(Messages.ENTER_NEWS_ID);
            Long newsId = inputReader.nextLong();
            var newsResponse = this.newsService.getNewsById(newsId);
            newsResponse.ifPresent(NewsDisplayer::displaySingleNews);

            consoleLogger.info(Messages.ADD_MORE);
            choice = inputReader.next().charAt(0);
        }
    }

}
