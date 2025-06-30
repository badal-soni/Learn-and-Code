package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NewsService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger = new ConsoleLogger();

    public NewsService() {
        this.httpClient = new HttpClient();
    }

    public void saveNews(long newsId) {
        String url = ApiUrls.NEWS + '/' + newsId + "/save";
        sendRequest(url);
    }

    public void unSaveNews(long newsId) {
        String url = ApiUrls.NEWS + '/' + newsId + "/unsave";
        sendRequest(url);
    }

    public void toggleNewsLike(long newsId) {
        String url = ApiUrls.NEWS + '/' + newsId + "/toggle-like";
        sendRequest(url);
    }

    public void dislikeNews(long newsId) {
        String url = ApiUrls.NEWS + '/' + newsId + "/dislike";
        sendRequest(url);
    }

    private void sendRequest(String url) {
        try {
            ApiResponse<Void> likeNewsResponse = httpClient.post(
                    url,
                    null,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    Void.class
            );
            if (!likeNewsResponse.isSuccess()) {
                throw new Exception("Failed to perform action: " + url);
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public List<NewsResponse> viewTodaysNews() {
        try {
            ApiResponse<List<NewsResponse>> response = httpClient.getList(
                    ApiUrls.TODAY_NEWS,
                    Map.of("Authorization", "Bearer " + UserContextHolder.accessToken),
                    NewsResponse.class
            );
            if (response.isSuccess() && response.getData() != null) {
                return response.getData();
            } else {
                throw new Exception("Failed to retrieve today's news.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public List<NewsResponse> viewUnderCategoryBetweenDates(
            String category,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        try {
            String url = ApiUrls.HEADLINES + "?from=" + fromDate + "&to=" + toDate;
            if (category.equals(Constants.ALL_CATEGORIES)) {
                return this.getAllNews(url);
            }

            url = ApiUrls.NEWS + '/' + category + "?from=" + fromDate + "&to=" + toDate;
            ApiResponse<List<NewsResponse>> response = httpClient.getList(
                    url,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    NewsResponse.class
            );
            if (response.isSuccess() && response.getData() != null) {
                return response.getData();
            } else {
                throw new Exception("Failed to retrieve news between dates.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    private List<NewsResponse> getAllNews(String url) {
        try {
            ApiResponse<List<NewsResponse>> response = httpClient.getList(
                    url,
                    Map.of("Authorization", "Bearer " + UserContextHolder.accessToken),
                    NewsResponse.class
            );
            if (response.isSuccess() && response.getData() != null) {
                return response.getData();
            } else {
                throw new Exception("Failed to retrieve all news.");
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public List<com.intimetec.newsaggregation.dto.response.NewsResponse> searchNews(SearchNewsRequest searchNewsRequest) {
        try {
            ApiResponse<List<com.intimetec.newsaggregation.dto.response.NewsResponse>> response = httpClient.postList(
                    ApiUrls.SEARCH_NEWS,
                    searchNewsRequest,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    com.intimetec.newsaggregation.dto.response.NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to search news")
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public void reportNews(
            Long newsId,
            ReportNewsArticleRequest reportNewsArticleRequest
    ) {
        try {
            String reportNewsApi = String.format(ApiUrls.REPORT_NEWS, newsId);
            this.httpClient.post(
                    reportNewsApi,
                    reportNewsArticleRequest,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    Void.class
            );
        } catch (Exception exception) {
            consoleLogger.info(exception.getMessage());
        }
    }

    public List<ReportedNewsResponse> getAllReportedNews() {
        try {
            ApiResponse<List<ReportedNewsResponse>> response = this.httpClient.getList(
                    ApiUrls.REPORTED_NEWS,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    ReportedNewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to get data")
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public void hideNews(NewsIds newsIds) {
        toggleNewsHiddenStatus(ApiUrls.HIDE_NEWS, newsIds);
    }

    public void unhideNews(NewsIds newsIds) {
        toggleNewsHiddenStatus(ApiUrls.UNHIDE_NEWS, newsIds);
    }

    private void toggleNewsHiddenStatus(
            String apiUrl,
            NewsIds newsIds
    ) {
        try {
            ApiResponse<Void> response = this.httpClient.put(
                    apiUrl,
                    newsIds,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Failed to unhide news: " + newsIds);
            }
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
    }

    public List<com.intimetec.newsaggregation.dto.response.NewsResponse> getAllHiddenNews() {
        try {
            ApiResponse<List<com.intimetec.newsaggregation.dto.response.NewsResponse>> response = this.httpClient.getList(
                    ApiUrls.HIDDEN_NEWS,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    com.intimetec.newsaggregation.dto.response.NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to get hidden news data")
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public Optional<com.intimetec.newsaggregation.dto.response.NewsResponse> getNewsById(Long newsId) {
        try {
            ApiResponse<com.intimetec.newsaggregation.dto.response.NewsResponse> response = this.httpClient.get(
                    ApiUrls.NEWS_ID,
                    Map.of(HttpHeader.AUTHORIZATION, Constants.BEARER + UserContextHolder.accessToken),
                    com.intimetec.newsaggregation.dto.response.NewsResponse.class
            );
            com.intimetec.newsaggregation.dto.response.NewsResponse newsResponse = CommonUtility.getDataOrElse(
                    response,
                    new Exception("Unable to fetch news by id")
            );
            return Optional.of(newsResponse);
        } catch (Exception exception) {

        }
        return Optional.empty();
    }

}
