package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;

import java.util.List;
import java.util.Objects;

public class NewsCategoryDisplayer {

    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int CATEGORY_NAME_WIDTH = 30;
    private static final int STATUS_WIDTH = 10;

    public static void displayNewsCategories(List<NewsCategoryResponse.NewsCategoryDetail> categories) {
        if (categories == null || categories.isEmpty()) {
            consoleLogger.info("No news categories available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nNews Categories:");
        consoleLogger.info(separator);
        System.out.printf(format, "CATEGORY NAME", "HIDDEN");
        consoleLogger.info(separator);

        for (NewsCategoryResponse.NewsCategoryDetail category : categories) {
            System.out.printf(
                    format,
                    truncateText(category.categoryName(), CATEGORY_NAME_WIDTH),
                    category.isHidden() ? "Yes" : "No"
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Categories: %d%n%n", categories.size());
    }

    private static String createFormat() {
        return String.format("| %%-%ds | %%-%ds |%%n",
                CATEGORY_NAME_WIDTH, STATUS_WIDTH);
    }

    private static String createSeparator() {
        int totalWidth = CATEGORY_NAME_WIDTH + STATUS_WIDTH + 7;
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
}