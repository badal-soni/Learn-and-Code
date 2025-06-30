package com.intimetec.newsaggregation.client.view;

import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.MenuChoices;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.service.NewsCategoryService;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;

import java.time.LocalDate;
import java.util.*;

public class HeadlineMenu implements MenuPresenter {

    private final NewsService newsService;
    private final NewsCategoryService newsCategoryService;
    private final ConsoleLogger consoleLogger;
    private final Scanner inputReader;

    private boolean isLoggedIn = false;

    public HeadlineMenu() {
        this.newsService = new NewsService();
        this.newsCategoryService = new NewsCategoryService();
        this.consoleLogger = new ConsoleLogger();
        this.inputReader = new Scanner(System.in);
    }

    @Override
    public void showMenu() {
        this.isLoggedIn = true;
        while (UserContextHolder.isLoggedIn) {
            if (renderHeadlineView()) {
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
        todaysNews.forEach(newsResponse -> {
            consoleLogger.info(newsResponse.toString());
        });
        this.viewNewsById();
        this.reportNews();
        this.likeNews();
    }

    private void viewHeadlinedInDateRange() {
        consoleLogger.info("Enter start date (yyyy-MM-dd): ");
        String startDate = inputReader.next();

        consoleLogger.info("Enter end date (yyyy-MM-dd): ");
        String endDate = inputReader.next();

        Optional<NewsCategoryResponse> newsCategoryResponses = this.newsCategoryService.getAllCategories();
        Map<Integer, String> categoryMap = getCategoryChoice(newsCategoryResponses.get());
        categoryMap.forEach((categoryId, categoryName) -> consoleLogger.info(categoryId + ". " + categoryName));

        consoleLogger.info("Enter the choice: ");
        int categoryChoice = inputReader.nextInt();

        List<NewsResponse> newsResponses = this.newsService.viewUnderCategoryBetweenDates(
                categoryMap.get(categoryChoice),
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        newsResponses.forEach(consoleLogger::info);
        this.viewNewsById();
        this.reportNews();
        this.likeNews();
        this.saveNews();;
    }

    private Map<Integer, String> getCategoryChoice(NewsCategoryResponse newsCategoryResponse) {
        Map<Integer, String> categoryMap = new TreeMap<>();
        categoryMap.put(1, Constants.ALL_CATEGORIES);

        final List<NewsCategoryResponse.NewsCategoryDetail> newsCategoryDetails = newsCategoryResponse.getNewsCategoryDetails();
        consoleLogger.info("Size: " + newsCategoryDetails.size());
        newsCategoryDetails.forEach(consoleLogger::info);
        for (int i = 2; i < newsCategoryDetails.size(); i++) {
            categoryMap.put(i, newsCategoryDetails.get(i).categoryName());
        }
        return categoryMap;
    }

    private void reportNews() {
        consoleLogger.info("Do you want to report a news (y/n) ?");
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info("Enter the news id to report: ");
            Long newsId = inputReader.nextLong();

            consoleLogger.info("Enter the report reason: ");
            String reportReason = inputReader.next();

            ReportNewsArticleRequest reportNewsArticleRequest = new ReportNewsArticleRequest();
            reportNewsArticleRequest.setReportReason(reportReason);

            this.newsService.reportNews(newsId, reportNewsArticleRequest);

            consoleLogger.info("Do you want to report more (y/n) ?");
            choice = inputReader.next().charAt(0);
        }
    }

    private void likeNews() {
        consoleLogger.info("Do you want to like a news (y/n) ?");
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info("Enter the news id to like: ");
            Long newsId = inputReader.nextLong();
            this.newsService.toggleNewsLike(newsId);

            consoleLogger.info("Do you want to like more (y/n) ?");
            choice = inputReader.next().charAt(0);
        }
    }

    public void saveNews() {
        consoleLogger.info("Do you want to save a news (y/n) ?");
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info("Enter the news id to save: ");
            Long newsId = inputReader.nextLong();
            this.newsService.saveNews(newsId);

            consoleLogger.info("Do you want to save more (y/n) ?");
            choice = inputReader.next().charAt(0);
        }
    }

    private void viewNewsById() {
        consoleLogger.info("Do you want to view a news by id (y/n) ?");
        char choice = inputReader.next().charAt(0);
        if (Character.toLowerCase(choice) != 'y') {
            return;
        }
        while (Character.toLowerCase(choice) == 'y') {
            consoleLogger.info("Enter the news id: ");
            Long newsId = inputReader.nextLong();
            var newsResponse = this.newsService.getNewsById(newsId);
            newsResponse.ifPresent(consoleLogger::info);
        }
    }

}
