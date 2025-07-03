package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;

import java.util.List;
import java.util.Objects;

public class KeywordDisplayer {
    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int ID_WIDTH = 5;
    private static final int KEYWORD_WIDTH = 30;
    private static final int CATEGORY_WIDTH = 25;
    private static final int STATUS_WIDTH = 10;

    public static void displayKeywords(List<KeywordResponse> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            consoleLogger.info("No keywords available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nKeywords List:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "KEYWORD", "CATEGORY", "STATUS");
        consoleLogger.info(separator);

        for (KeywordResponse keyword : keywords) {
            System.out.printf(format,
                    keyword.getKeywordId(),
                    truncateText(keyword.getKeyword(), KEYWORD_WIDTH),
                    truncateText(keyword.getParentCategory(), CATEGORY_WIDTH),
                    keyword.isEnabled() ? "Enabled" : "Disabled"
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Keywords: %d%n%n", keywords.size());
    }

    public static void displaySingleKeyword(KeywordResponse keyword) {
        if (keyword == null) {
            consoleLogger.info("No keyword available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nKeyword Details:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "KEYWORD", "CATEGORY", "STATUS");
        consoleLogger.info(separator);
        System.out.printf(format,
                keyword.getKeywordId(),
                truncateText(keyword.getKeyword(), KEYWORD_WIDTH),
                truncateText(keyword.getParentCategory(), CATEGORY_WIDTH),
                keyword.isEnabled() ? "Enabled" : "Disabled"
        );
        consoleLogger.info(separator);
    }

    private static String createFormat() {
        return String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, KEYWORD_WIDTH, CATEGORY_WIDTH, STATUS_WIDTH);
    }

    private static String createSeparator() {
        int totalWidth = ID_WIDTH + KEYWORD_WIDTH + CATEGORY_WIDTH + STATUS_WIDTH + 13;
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