package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.dto.response.NotificationConfigurationResponse;

import java.util.List;
import java.util.Objects;

public class NotificationConfigurationsDisplayer {
    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int CATEGORY_WIDTH = 20;
    private static final int ENABLED_WIDTH = 10;
    private static final int KEYWORDS_WIDTH = 70;

    public static void displayConfigurations(AllNotificationConfigurations configurations) {
        if (configurations == null || configurations.getNewsCategories() == null || configurations.getNewsCategories().isEmpty()) {
            consoleLogger.info("No notification configurations available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nNotification Configurations for User ID: " + configurations.getUserId());
        consoleLogger.info(separator);
        System.out.printf(format, "CATEGORY", "ENABLED", "KEYWORDS (✓: Active, ✗: Inactive)");
        consoleLogger.info(separator);

        for (NotificationConfigurationResponse config : configurations.getNewsCategories()) {
            System.out.printf(format,
                    truncateText(config.getCategoryName(), CATEGORY_WIDTH),
                    config.isEnabled() ? "Yes" : "No",
                    formatKeywordsWithStatus(config.getKeywords())
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Categories: %d%n%n", configurations.getNewsCategories().size());
    }

    private static String createFormat() {
        return String.format("| %%-%ds | %%-%ds | %%-%ds |%%n",
                CATEGORY_WIDTH, ENABLED_WIDTH, KEYWORDS_WIDTH);
    }

    private static String createSeparator() {
        int totalWidth = CATEGORY_WIDTH + ENABLED_WIDTH + KEYWORDS_WIDTH + 10;
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

    private static String formatKeywordsWithStatus(List<KeywordResponse> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return "N/A";
        }

        List<String> keywordsWithStatus = keywords.stream()
                .map(keyword -> String.format("%s%s",
                        keyword.getKeyword(),
                        keyword.isEnabled() ? "(✓)" : "(✗)"))
                .toList();

        String keywordsStr = String.join(", ", keywordsWithStatus);
        return truncateText(keywordsStr, KEYWORDS_WIDTH);
    }
}