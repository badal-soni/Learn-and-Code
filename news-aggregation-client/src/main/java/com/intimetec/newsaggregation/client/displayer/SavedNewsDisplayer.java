package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;

import java.util.List;
import java.util.Objects;

public class SavedNewsDisplayer {

    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int ID_WIDTH = 5;
    private static final int HEADLINE_WIDTH = 40;
    private static final int SOURCE_WIDTH = 20;
    private static final int MIN_URL_WIDTH = 20;
    private static final int CATEGORIES_WIDTH = 30;

    public static void displaySavedNews(List<SavedNewsResponse> newsList) {
        if (newsList == null || newsList.isEmpty()) {
            consoleLogger.info("No saved news available.");
            return;
        }

        int urlWidth = calculateUrlWidth(newsList);
        String format = createFormat(urlWidth);
        String separator = createSeparator(urlWidth);

        consoleLogger.info("\nSaved News:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "HEADLINE", "SOURCE", "URL", "CATEGORIES");
        consoleLogger.info(separator);

        for (SavedNewsResponse news : newsList) {
            System.out.printf(format,
                    news.getId(),
                    truncateText(news.getHeadline(), HEADLINE_WIDTH),
                    truncateText(news.getSource(), SOURCE_WIDTH),
                    news.getUrl(),
                    formatCategories(news.getCategories())
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Saved News Items: %d%n%n", newsList.size());
    }

    private static int calculateUrlWidth(List<SavedNewsResponse> newsList) {
        return newsList.stream()
                .map(SavedNewsResponse::getUrl)
                .filter(Objects::nonNull)
                .mapToInt(String::length)
                .max()
                .orElse(MIN_URL_WIDTH);
    }

    private static String createFormat(int urlWidth) {
        return String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, HEADLINE_WIDTH, SOURCE_WIDTH, urlWidth, CATEGORIES_WIDTH);
    }

    private static String createSeparator(int urlWidth) {
        int totalWidth = ID_WIDTH + HEADLINE_WIDTH + SOURCE_WIDTH + urlWidth + CATEGORIES_WIDTH + 14;
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

    public static void displaySingleSavedNews(SavedNewsResponse news) {
        if (news == null) {
            consoleLogger.info("No saved news available.");
            return;
        }

        String format = createFormat(news.getUrl().length());
        String separator = createSeparator(news.getUrl().length());

        consoleLogger.info("\nSaved News Details:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "HEADLINE", "SOURCE", "URL", "CATEGORIES");
        consoleLogger.info(separator);
        System.out.printf(format,
                news.getId(),
                truncateText(news.getHeadline(), HEADLINE_WIDTH),
                truncateText(news.getSource(), SOURCE_WIDTH),
                news.getUrl(),
                formatCategories(news.getCategories())
        );
        consoleLogger.info(separator);
    }
}