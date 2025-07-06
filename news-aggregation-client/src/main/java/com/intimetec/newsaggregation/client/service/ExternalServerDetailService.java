package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.dto.request.CreateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.request.UpdateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;

import java.util.List;
import java.util.Map;

public class ExternalServerDetailService {

    private final HttpClient httpClient;
    private final FileLogger fileLogger;

    public ExternalServerDetailService() {
        this.httpClient = new HttpClient();
        this.fileLogger = FileLogger.getInstance();
    }

    public void registerExternalServer(CreateExternalServerRequest createExternalServerRequest) {
        try {
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            ApiResponse<Void> response = httpClient.post(
                    ApiUrls.EXTERNAL_SERVER,
                    createExternalServerRequest,
                    headers,
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
        } catch (Exception exception) {
            fileLogger.error(ExternalServerDetailService.class + ": " + exception.getMessage());
        }
    }

    public List<ExternalServerStatusResponse> getExternalServerStatus() {
        return getExternalServerDetails();
    }

    public List<ExternalServerStatusResponse> getExternalServerApiKeyDetails() {
        return getExternalServerDetails();
    }

    private List<ExternalServerStatusResponse> getExternalServerDetails() {
        try {
            ApiResponse<List<ExternalServerStatusResponse>> response = httpClient.getList(
                    ApiUrls.EXTERNAL_SERVER,
                    CommonUtility.getDefaultHeaders(),
                    ExternalServerStatusResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Failed to retrieve external servers.")
            );
        } catch (Exception exception) {
            fileLogger.error(ExternalServerDetailService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

    public void updateExternalServer(UpdateExternalServerRequest updateExternalServerRequest) {
        try {
            final Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            ApiResponse<Void> response = httpClient.put(
                    ApiUrls.EXTERNAL_SERVER,
                    updateExternalServerRequest,
                    headers,
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
        } catch (Exception exception) {
            fileLogger.error(ExternalServerDetailService.class + ": " + exception.getMessage());
        }
    }

}
