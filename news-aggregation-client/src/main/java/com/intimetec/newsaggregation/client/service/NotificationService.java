package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;

import java.util.List;

public class NotificationService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;

    public NotificationService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
    }

    public List<NotificationResponse> getAllNotifications() {
        try {
            String url = ApiUrls.NOTIFICATIONS;
            ApiResponse<List<NotificationResponse>> notifications = httpClient.getList(
                    url,
                    null,
                    NotificationResponse.class
            );
            if (notifications.isSuccess() && notifications.getData() != null) {
                return notifications.getData();
            } else {
                throw new Exception("Failed to retrieve notifications.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

}
