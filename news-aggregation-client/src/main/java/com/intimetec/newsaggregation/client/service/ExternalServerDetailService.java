package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.request.CreateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.request.UpdateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.ViewExternalServerResponse;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;

import java.util.List;
import java.util.Map;

public class ExternalServerDetailService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;

    public ExternalServerDetailService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
    }

    public void registerExternalServer(CreateExternalServerRequest createExternalServerRequest) {
        try {
            ApiResponse<Void> response = httpClient.post(
                    ApiUrls.EXTERNAL_SERVER,
                    createExternalServerRequest,
                    Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON, "Authorization", "Bearer " + UserContextHolder.accessToken),
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public void viewExternalServers() {
        try {
            ApiResponse<List<ViewExternalServerResponse>> response = httpClient.getList(
                    ApiUrls.EXTERNAL_SERVER,
                    Map.of("Authorization", "Bearer " + UserContextHolder.accessToken),
                    ViewExternalServerResponse.class
            );
            if (response.isSuccess() && response.getData() != null) {
                for (ViewExternalServerResponse server : response.getData()) {
                    consoleLogger.info(server.viewExternalServerDetail());
                }
            } else {
                throw new Exception("Failed to retrieve external servers.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public void viewApiKey() {
        try {
            ApiResponse<List<ViewExternalServerResponse>> response = httpClient.getList(
                    ApiUrls.EXTERNAL_SERVER,
                    Map.of("Authorization", "Bearer " + UserContextHolder.accessToken),
                    ViewExternalServerResponse.class
            );
            if (response.isSuccess() && response.getData() != null) {
                for (ViewExternalServerResponse server : response.getData()) {
                    consoleLogger.info(server.viewApiKey());
                }
            } else {
                throw new Exception("Failed to retrieve external servers.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public void updateExternalServer(UpdateExternalServerRequest updateExternalServerRequest) {
        try {
            ApiResponse<Void> response = httpClient.put(
                    ApiUrls.EXTERNAL_SERVER,
                    updateExternalServerRequest,
                    Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON, "Authorization", "Bearer " + UserContextHolder.accessToken),
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

}
