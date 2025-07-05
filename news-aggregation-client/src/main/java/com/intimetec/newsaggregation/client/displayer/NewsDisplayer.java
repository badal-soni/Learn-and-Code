package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;

import java.util.List;
import java.util.Objects;

public class NewsDisplayer {
    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int ID_WIDTH = 5;
    private static final int HEADLINE_WIDTH = 40;
    private static final int CATEGORIES_WIDTH = 30;
    private static final int MIN_URL_WIDTH = 20;

    public static void displayNews(List<NewsResponse> newsList) {
        if (newsList == null || newsList.isEmpty()) {
            consoleLogger.info("No news available.");
            return;
        }

        int urlWidth = calculateUrlWidth(newsList);
        String format = createFormat(urlWidth);
        String separator = createSeparator(urlWidth);

        consoleLogger.info("\nNews Feed:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "HEADLINE", "CATEGORIES", "URL");
        consoleLogger.info(separator);

        for (NewsResponse news : newsList) {
            System.out.printf(format,
                    news.getId(),
                    truncateText(news.getHeadline(), HEADLINE_WIDTH),
                    formatCategories(news.getCategories()),
                    news.getUrl()
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total News Items: %d%n%n", newsList.size());
    }

    private static int calculateUrlWidth(List<NewsResponse> newsList) {
        return newsList.stream()
                .map(NewsResponse::getUrl)
                .filter(Objects::nonNull)
                .mapToInt(String::length)
                .max()
                .orElse(MIN_URL_WIDTH);
    }

    private static String createFormat(int urlWidth) {
        return String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, HEADLINE_WIDTH, CATEGORIES_WIDTH, urlWidth);
    }

    private static String createSeparator(int urlWidth) {
        int totalWidth = ID_WIDTH + HEADLINE_WIDTH + CATEGORIES_WIDTH + urlWidth + 12;
        return "+" + "-".repeat(totalWidth) + "+";
    }

    private static String truncateText(String text, int maxWidth) {
        final String NOT_ANY_CONTENT = "N/A";
        if (Objects.isNull(text)) {
            return NOT_ANY_CONTENT;
        }
        return text.length() > maxWidth - 3
                ? text.substring(0, maxWidth - 3) + "..."
                : text;
    }

    private static String formatCategories(List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return "N/A";
        }
        String categoriesStr = String.join(", ", categories);
        return truncateText(categoriesStr, CATEGORIES_WIDTH);
    }

    public static void displaySingleNews(com.intimetec.newsaggregation.dto.response.NewsResponse news) {
        if (news == null) {
            consoleLogger.info("No news available.");
            return;
        }

        String format = createFormat(news.getUrl().length());
        String separator = createSeparator(news.getUrl().length());

        consoleLogger.info("\nNews Details:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "HEADLINE", "CATEGORIES", "URL");
        consoleLogger.info(separator);
        System.out.printf(format,
                news.getId(),
                truncateText(news.getHeadline(), HEADLINE_WIDTH),
                formatCategories(news.getCategories()),
                news.getUrl()
        );
        consoleLogger.info(separator);
    }
}