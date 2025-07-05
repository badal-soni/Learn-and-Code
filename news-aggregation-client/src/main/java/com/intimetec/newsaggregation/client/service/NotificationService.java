package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.NotificationResponse;

import java.util.List;

public class NotificationService {

    private final HttpClient httpClient;
    private final FileLogger fileLogger;

    public NotificationService() {
        this.httpClient = new HttpClient();
        this.fileLogger = FileLogger.getInstance();
    }

    public List<NotificationResponse> getAllNotifications() {
        try {
            String url = ApiUrls.NOTIFICATIONS;
            ApiResponse<List<com.intimetec.newsaggregation.dto.response.NotificationResponse>> notifications = httpClient.getList(
                    url,
                    CommonUtility.getDefaultHeaders(),
                    com.intimetec.newsaggregation.dto.response.NotificationResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    notifications,
                    new Exception("Got error: " + notifications.getData())
            );
        } catch (Exception exception) {
            fileLogger.error(NotificationService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

}
