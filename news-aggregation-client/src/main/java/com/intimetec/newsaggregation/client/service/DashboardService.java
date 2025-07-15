package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;

import java.util.List;

public class DashboardService {

    private final HttpClient httpClient;
    private final FileLogger fileLogger;

    public DashboardService() {
        this.httpClient = new HttpClient();
        this.fileLogger = FileLogger.getInstance();
    }

    public List<SavedNewsResponse> getSavedNews() {
        try {
            ApiResponse<List<SavedNewsResponse>> response = this.httpClient.getList(
                    ApiUrls.SAVED_NEWS,
                    CommonUtility.getDefaultHeaders(),
                    SavedNewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to fetch the saved news")
            );
        } catch (Exception exception) {
            fileLogger.error(DashboardService.class + ": " + exception.getMessage());
        }
        return List.of();
    }

}
