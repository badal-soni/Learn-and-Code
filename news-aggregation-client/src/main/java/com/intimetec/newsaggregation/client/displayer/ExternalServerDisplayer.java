package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;

import java.util.List;
import java.util.Objects;

public class ExternalServerDisplayer {
    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int ID_WIDTH = 5;
    private static final int SERVER_NAME_WIDTH = 30;
    private static final int STATUS_WIDTH = 15;
    private static final int LAST_ACCESSED_WIDTH = 25;
    private static final int API_KEY_WIDTH = 50;

    public static void displayServerDetails(List<ExternalServerStatusResponse> servers) {
        if (servers == null || servers.isEmpty()) {
            consoleLogger.info("No external servers available.");
            return;
        }

        String format = createServerDetailsFormat();
        String separator = createServerDetailsSeparator();

        consoleLogger.info("\nExternal Server Details:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "SERVER NAME", "STATUS", "LAST ACCESSED");
        consoleLogger.info(separator);

        for (ExternalServerStatusResponse server : servers) {
            String lastAccessedDate = "N/A";
            if (Objects.nonNull(server.getLastAccessedDate())) {
                lastAccessedDate = server.getLastAccessedDate().toString();
            }
            System.out.printf(format,
                    server.getServerId(),
                    truncateText(server.getServerName(), SERVER_NAME_WIDTH),
                    server.isActiveStatus() ? "Active" : "Not Active",
                    truncateText(lastAccessedDate, LAST_ACCESSED_WIDTH)
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Servers: %d%n%n", servers.size());
    }

    public static void displayServerApiKeys(List<ExternalServerStatusResponse> servers) {
        if (servers == null || servers.isEmpty()) {
            consoleLogger.info("No server API keys available.");
            return;
        }

        String format = createApiKeyFormat();
        String separator = createApiKeySeparator();

        consoleLogger.info("\nServer API Keys:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "SERVER NAME", "API KEY");
        consoleLogger.info(separator);

        for (ExternalServerStatusResponse server : servers) {
            System.out.printf(format,
                    server.getServerId(),
                    truncateText(server.getServerName(), SERVER_NAME_WIDTH),
                    truncateText(server.getApiKey(), API_KEY_WIDTH)
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Servers: %d%n%n", servers.size());
    }

    private static String createServerDetailsFormat() {
        return String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, SERVER_NAME_WIDTH, STATUS_WIDTH, LAST_ACCESSED_WIDTH);
    }

    private static String createApiKeyFormat() {
        return String.format("| %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, SERVER_NAME_WIDTH, API_KEY_WIDTH);
    }

    private static String createServerDetailsSeparator() {
        int totalWidth = ID_WIDTH + SERVER_NAME_WIDTH + STATUS_WIDTH + LAST_ACCESSED_WIDTH + 13;
        return "+" + "-".repeat(totalWidth) + "+";
    }

    private static String createApiKeySeparator() {
        int totalWidth = ID_WIDTH + SERVER_NAME_WIDTH + API_KEY_WIDTH + 10;
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

    public static void displayCompleteServerDetails(List<ExternalServerStatusResponse> servers) {
        if (servers == null || servers.isEmpty()) {
            consoleLogger.info("No external servers available.");
            return;
        }

        String format = String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, SERVER_NAME_WIDTH, STATUS_WIDTH, LAST_ACCESSED_WIDTH, API_KEY_WIDTH);

        int totalWidth = ID_WIDTH + SERVER_NAME_WIDTH + STATUS_WIDTH + LAST_ACCESSED_WIDTH + API_KEY_WIDTH + 16;
        String separator = "+" + "-".repeat(totalWidth) + "+";

        consoleLogger.info("\nComplete External Server Details:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "SERVER NAME", "STATUS", "LAST ACCESSED", "API KEY");
        consoleLogger.info(separator);

        for (ExternalServerStatusResponse server : servers) {
            String lastAccessedDate = "N/A";
            if (Objects.nonNull(server.getLastAccessedDate())) {
                lastAccessedDate = server.getLastAccessedDate().toString();
            }

            System.out.printf(format,
                    server.getServerId(),
                    truncateText(server.getServerName(), SERVER_NAME_WIDTH),
                    server.isActiveStatus() ? "Active" : "Not Active",
                    truncateText(lastAccessedDate, LAST_ACCESSED_WIDTH),
                    truncateText(server.getApiKey(), API_KEY_WIDTH)
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Servers: %d%n%n", servers.size());
    }

}