package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.dto.NewsCategories;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NewsCategoryService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;

    public NewsCategoryService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
    }

    public void addCategory(CreateCategoryRequest createCategoryRequest) {
        try {
            ApiResponse<Void> createCategoryResponse = httpClient.post(
                    ApiUrls.CATEGORIES,
                    createCategoryRequest,
                    Map.of("Authorization", "Bearer " + UserContextHolder.accessToken, HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    Void.class
            );
            if (!createCategoryResponse.isSuccess()) {
                throw new Exception("Failed to create category: " + createCategoryResponse.getData());
            }
            consoleLogger.info("Category created successfully.");
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public Optional<NewsCategoryResponse> getAllCategories() {
        try {
            ApiResponse<NewsCategoryResponse> categories = httpClient.get(
                    ApiUrls.CATEGORIES,
                    Map.of("Authorization", "Bearer " + UserContextHolder.accessToken),
                    NewsCategoryResponse.class
            );
            NewsCategoryResponse newsCategoryResponse = CommonUtility.getDataOrElseThrow(
                    categories,
                    new Exception("Unable to fetch categories")
            );
            return Optional.of(newsCategoryResponse);
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return Optional.empty();
    }

    public void hideNewsCategories(List<String> newsCategories) {
        try {
            NewsCategories newsCategoriesRequest = new NewsCategories();
            newsCategoriesRequest.setNewsCategories(newsCategories);
            this.httpClient.put(
                    ApiUrls.HIDE_CATEGORY,
                    newsCategoriesRequest,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken, HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    Void.class
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public Optional<NewsCategoryResponse> getAllHiddenNewsCategories() {
        try {
            ApiResponse<NewsCategoryResponse> response = this.httpClient.get(
                    ApiUrls.HIDDEN_CATEGORIES,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    NewsCategoryResponse.class
            );
            NewsCategoryResponse newsCategoryResponse = CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to find hidden news categories")
            );
            return Optional.of(newsCategoryResponse);
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return Optional.empty();
    }

}
