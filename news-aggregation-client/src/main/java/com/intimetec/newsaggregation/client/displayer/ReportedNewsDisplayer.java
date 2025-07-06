package com.intimetec.newsaggregation.client.displayer;

import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;

import java.util.List;
import java.util.Objects;

public class ReportedNewsDisplayer {
    private static final ConsoleLogger consoleLogger = new ConsoleLogger();
    private static final int ID_WIDTH = 5;
    private static final int REPORT_DESC_WIDTH = 40;
    private static final int USER_EMAIL_WIDTH = 30;
    private static final int REPORTED_AT_WIDTH = 15;
    private static final int STATUS_WIDTH = 10;

    public static void displayReportedNews(List<ReportedNewsResponse> reportedNews) {
        if (reportedNews == null || reportedNews.isEmpty()) {
            consoleLogger.info("No reported news available.");
            return;
        }

        String format = createFormat();
        String separator = createSeparator();

        consoleLogger.info("\nReported News List:");
        consoleLogger.info(separator);
        System.out.printf(format, "ID", "REPORT REASON", "REPORTED BY", "REPORTED AT", "HIDDEN");
        consoleLogger.info(separator);

        for (ReportedNewsResponse news : reportedNews) {
            String reportedAt = "N/A";
            if (Objects.nonNull(news.getReportedAt())) {
                reportedAt = news.getReportedAt().toString();
            }
            System.out.printf(format,
                    news.getNewsId(),
                    truncateText(news.getReportReason(), REPORT_DESC_WIDTH),
                    truncateText(news.getReportedByUserEmail(), USER_EMAIL_WIDTH),
                    truncateText(reportedAt, REPORTED_AT_WIDTH),
                    news.isHidden() ? "Yes" : "No"
            );
        }

        consoleLogger.info(separator);
        System.out.printf("Total Reported News: %d%n%n", reportedNews.size());
    }

    private static String createFormat() {
        return String.format("| %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds |%%n",
                ID_WIDTH, REPORT_DESC_WIDTH, USER_EMAIL_WIDTH, REPORTED_AT_WIDTH, STATUS_WIDTH);
    }

    private static String createSeparator() {
        int totalWidth = ID_WIDTH + REPORT_DESC_WIDTH + USER_EMAIL_WIDTH + REPORTED_AT_WIDTH + STATUS_WIDTH + 16;
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