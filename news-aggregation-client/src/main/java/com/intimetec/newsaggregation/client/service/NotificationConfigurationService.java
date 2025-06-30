package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotificationConfigurationService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;

    public NotificationConfigurationService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
    }

    public AllNotificationConfigurations getAllNotificationConfigurations() {
        try {
            ApiResponse<AllNotificationConfigurations> configurations = httpClient.get(
                    ApiUrls.NOTIFICATION_CONFIGURATIONS,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    AllNotificationConfigurations.class
            );
            if (Objects.nonNull(configurations.getData())) {
                return configurations.getData();
            } else {
                throw new Exception("Failed to retrieve notification configurations.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return new AllNotificationConfigurations();
    }

    public void updateNotificationPreferences(UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest) {
        try {
            ApiResponse<Void> response = httpClient.put(
                    ApiUrls.NOTIFICATION_CONFIGURATIONS,
                    updateNotificationPreferencesRequest,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Failed to update notification preferences.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public List<KeywordResponse> getAllKeywordsOfUser() {
        try {
            ApiResponse<List<KeywordResponse>> response = this.httpClient.getList(
                    ApiUrls.KEYWORDS,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    KeywordResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to fetch the keywords")
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public void toggleKeywordActiveStatus(Long keywordId) {
        try {
            this.httpClient.put(
                    ApiUrls.TOGGLE_KEYWORD_ACTIVE_STATUS,
                    null,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    Void.class
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public void addKeyword(CreateKeywordRequest createKeywordRequest) {
        try {
            this.httpClient.post(
                    ApiUrls.KEYWORDS,
                    createKeywordRequest,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    Void.class
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

}
