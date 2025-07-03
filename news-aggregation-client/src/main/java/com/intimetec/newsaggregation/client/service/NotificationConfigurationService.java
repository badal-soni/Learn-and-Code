package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;

import java.util.List;
import java.util.Map;

public class NotificationConfigurationService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;
    private final FileLogger fileLogger;

    public NotificationConfigurationService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
        this.fileLogger = FileLogger.getInstance();
    }

    public AllNotificationConfigurations getAllNotificationConfigurations() {
        try {
            ApiResponse<AllNotificationConfigurations> response = httpClient.get(
                    ApiUrls.NOTIFICATION_CONFIGURATIONS,
                    CommonUtility.getDefaultHeaders(),
                    AllNotificationConfigurations.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to fetch notification configurations.")
            );
        } catch (Exception exception) {
            fileLogger.error(NotificationConfigurationService.class + ": " + exception.getMessage());
            return new AllNotificationConfigurations();
        }
    }

    public void updateNotificationPreferences(UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest) {
        try {
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            ApiResponse<Void> response = httpClient.put(
                    ApiUrls.NOTIFICATION_CONFIGURATIONS,
                    updateNotificationPreferencesRequest,
                    headers,
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Failed to update notification preferences.");
            }
        } catch (Exception exception) {
            fileLogger.error(NotificationConfigurationService.class + ": " + exception.getMessage());
        }
    }

    public List<KeywordResponse> getAllKeywordsOfUser() {
        try {
            ApiResponse<List<KeywordResponse>> response = this.httpClient.getList(
                    ApiUrls.KEYWORDS,
                    CommonUtility.getDefaultHeaders(),
                    KeywordResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to fetch the keywords")
            );
        } catch (Exception exception) {
            fileLogger.error(NotificationConfigurationService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

    public void toggleKeywordActiveStatus(Long keywordId) {
        try {
            final String apiUrl = String.format(ApiUrls.TOGGLE_KEYWORD_ACTIVE_STATUS, keywordId);
            this.httpClient.put(
                    apiUrl,
                    null,
                    CommonUtility.getDefaultHeaders(),
                    Void.class
            );
        } catch (Exception exception) {
            fileLogger.error(NotificationConfigurationService.class + ": " + exception.getMessage());
        }
    }

    public void addKeyword(CreateKeywordRequest createKeywordRequest) {
        try {
            this.httpClient.post(
                    ApiUrls.KEYWORDS,
                    createKeywordRequest,
                    CommonUtility.getDefaultHeaders(),
                    Void.class
            );
        } catch (Exception exception) {
            fileLogger.error(NotificationConfigurationService.class + ": " + exception.getMessage());
        }
    }

}
