package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;

import java.util.List;
import java.util.Map;

public class DashboardService {

    private final NewsService newsService;
    private final HttpClient httpClient;

    public DashboardService() {
        this.newsService = new NewsService();
        this.httpClient = new HttpClient();
    }

    public List<SavedNewsResponse> getSavedNews() {
        try {
            ApiResponse<List<SavedNewsResponse>> response = this.httpClient.getList(
                    ApiUrls.SAVED_NEWS,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    SavedNewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to fetch the saved news")
            );
        } catch (Exception exception) {
            // todo: log in file instead
        }
        return List.of();
    }

}
