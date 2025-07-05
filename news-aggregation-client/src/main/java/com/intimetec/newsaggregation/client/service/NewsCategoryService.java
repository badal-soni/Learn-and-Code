package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.NewsCategories;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NewsCategoryService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;
    private final FileLogger fileLogger;

    public NewsCategoryService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
        this.fileLogger = FileLogger.getInstance();
    }

    public void addCategory(CreateCategoryRequest createCategoryRequest) {
        try {
            final Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            ApiResponse<Void> createCategoryResponse = httpClient.post(
                    ApiUrls.CATEGORIES,
                    createCategoryRequest,
                    headers,
                    Void.class
            );
            if (!createCategoryResponse.isSuccess()) {
                throw new Exception("Failed to create category: " + createCategoryResponse.getData());
            }
            consoleLogger.info("Category created successfully.");
        } catch (Exception exception) {
            fileLogger.error(NewsCategoryService.class + ": " + exception.getMessage());
        }
    }


    public Optional<NewsCategoryResponse> getAllCategories() {
        try {
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            ApiResponse<NewsCategoryResponse> categories = httpClient.get(
                    ApiUrls.UNHIDDEN_CATEGORIES,
                    headers,
                    NewsCategoryResponse.class
            );
            NewsCategoryResponse newsCategoryResponse = CommonUtility.getDataOrElseThrow(
                    categories,
                    new Exception("Unable to fetch categories")
            );
            return Optional.of(newsCategoryResponse);
        } catch (Exception exception) {
            fileLogger.error(NewsCategoryService.class + ": " + exception.getMessage());
        }
        return Optional.empty();
    }

    public void hideNewsCategories(List<String> newsCategories) {
        try {
            NewsCategories newsCategoriesRequest = new NewsCategories();
            newsCategoriesRequest.setNewsCategories(newsCategories);
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            this.httpClient.put(
                    ApiUrls.HIDE_CATEGORY,
                    newsCategoriesRequest,
                    headers,
                    Void.class
            );
        } catch (Exception exception) {
            fileLogger.error(NewsCategoryService.class + ": " + exception.getMessage());
        }
    }

    public void unHideNewsCategories(List<String> newsCategories) {
        try {
            NewsCategories newsCategoriesRequest = new NewsCategories();
            newsCategoriesRequest.setNewsCategories(newsCategories);
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            this.httpClient.put(
                    ApiUrls.UNHIDE_CATEGORY,
                    newsCategoriesRequest,
                    headers,
                    Void.class
            );
        } catch (Exception exception) {
            fileLogger.error(NewsCategoryService.class + ": " + exception.getMessage());
        }
    }

    public Optional<NewsCategoryResponse> getAllHiddenNewsCategories() {
        try {
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            ApiResponse<NewsCategoryResponse> response = this.httpClient.get(
                    ApiUrls.HIDDEN_CATEGORIES,
                    headers,
                    NewsCategoryResponse.class
            );
            NewsCategoryResponse newsCategoryResponse = CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to find hidden news categories")
            );
            return Optional.of(newsCategoryResponse);
        } catch (Exception exception) {
            fileLogger.error(NewsCategoryService.class + ": " + exception.getMessage());
        }
        return Optional.empty();
    }

    public Optional<NewsCategoryResponse> getAllUnHiddenNewsCategories() {
        try {
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            ApiResponse<NewsCategoryResponse> response = this.httpClient.get(
                    ApiUrls.UNHIDDEN_CATEGORIES,
                    headers,
                    NewsCategoryResponse.class
            );
            NewsCategoryResponse newsCategoryResponse = CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to find hidden news categories")
            );
            return Optional.of(newsCategoryResponse);
        } catch (Exception exception) {
            fileLogger.error(NewsCategoryService.class + ": " + exception.getMessage());
        }
        return Optional.empty();
    }

}
