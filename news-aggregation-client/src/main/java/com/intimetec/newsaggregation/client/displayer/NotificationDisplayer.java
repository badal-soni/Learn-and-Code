package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;

import java.util.List;
import java.util.Objects;

public class NotificationDisplayer {
    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int ID_WIDTH = 5;
    private static final int CONTENT_WIDTH = 80;

    public static void displayNotifications(List<NotificationResponse> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            consoleLogger.info("No notifications available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nNotifications:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "CONTENT");
        consoleLogger.info(separator);

        for (NotificationResponse notification : notifications) {
            System.out.printf(format,
                    notification.getNotificationId(),
                    truncateText(notification.getContent(), CONTENT_WIDTH)
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Notifications: %d%n%n", notifications.size());
    }

    private static String createFormat() {
        return String.format("| %%-%ds | %%-%ds |%%n", ID_WIDTH, CONTENT_WIDTH);
    }

    private static String createSeparator() {
        int totalWidth = ID_WIDTH + CONTENT_WIDTH + 6;
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

    public static void displaySingleNotification(NotificationResponse notification) {
        if (notification == null) {
            consoleLogger.info("No notification available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nNotification Details:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "CONTENT");
        consoleLogger.info(separator);
        System.out.printf(format,
                notification.getNotificationId(),
                truncateText(notification.getContent(), CONTENT_WIDTH)
        );
        consoleLogger.info(separator);
    }
}