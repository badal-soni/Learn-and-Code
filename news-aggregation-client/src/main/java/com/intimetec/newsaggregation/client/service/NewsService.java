package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.Constants;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NewsService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;
    private final FileLogger fileLogger;

    public NewsService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
        this.fileLogger = FileLogger.getInstance();
    }

    public void saveNews(long newsId) {
        String url = String.format(ApiUrls.SAVE_NEWS, newsId);
        sendRequest(url);
    }

    public void unSaveNews(long newsId) {
        try {
            final String url = String.format(ApiUrls.UNSAVE_NEWS, newsId);
            this.httpClient.delete(
                    url,
                    CommonUtility.getDefaultHeaders(),
                    null
            );
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
        }
    }

    public void toggleNewsLike(long newsId) {
        String url = String.format(ApiUrls.TOGGLE_LIKE, newsId);
        sendRequest(url);
    }

    private void sendRequest(String url) {
        try {
            ApiResponse<Void> likeNewsResponse = httpClient.post(
                    url,
                    null,
                    CommonUtility.getDefaultHeaders(),
                    Void.class
            );
            if (!likeNewsResponse.isSuccess()) {
                throw new Exception("Failed to perform action: " + url);
            }
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
        }
    }

    public List<NewsResponse> viewTodaysNews() {
        try {
            ApiResponse<List<NewsResponse>> response = httpClient.getList(
                    ApiUrls.TODAY_NEWS,
                    CommonUtility.getDefaultHeaders(),
                    NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to get today's news")
            );
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return List.of();
    }

    public List<NewsResponse> getAllNewsUnderCategoryBetweenDates(
            String category,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        try {
            String url = String.format(ApiUrls.DATE_RANGE_HEADLINES, fromDate, toDate);
            if (category.equals(Constants.ALL_CATEGORIES)) {
                return this.getAllNews(url);
            }

            url = String.format(ApiUrls.CATEGORY_HEADLINES, category, fromDate, toDate);
            ApiResponse<List<NewsResponse>> response = httpClient.getList(
                    url,
                    CommonUtility.getDefaultHeaders(),
                    NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to get news for category: " + category + " between dates: " + fromDate + " and " + toDate)
            );
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

    private List<NewsResponse> getAllNews(String url) {
        try {
            ApiResponse<List<NewsResponse>> response = httpClient.getList(
                    url,
                    CommonUtility.getDefaultHeaders(),
                    NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to get all news")
            );
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

    public List<com.intimetec.newsaggregation.client.dto.response.NewsResponse> searchNews(SearchNewsRequest searchNewsRequest) {
        try {
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            ApiResponse<List<com.intimetec.newsaggregation.client.dto.response.NewsResponse>> response = httpClient.postList(
                    ApiUrls.SEARCH_NEWS,
                    searchNewsRequest,
                    headers,
                    com.intimetec.newsaggregation.client.dto.response.NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to search news")
            );
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

    public void reportNews(
            Long newsId,
            ReportNewsArticleRequest reportNewsArticleRequest
    ) {
        try {
            String reportNewsApi = String.format(ApiUrls.REPORT_NEWS, newsId);
            Map<String, String> headers = CommonUtility.getDefaultHeaders();
            headers.put(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            this.httpClient.post(
                    reportNewsApi,
                    reportNewsArticleRequest,
                    headers,
                    Void.class
            );
        } catch (Exception exception) {
            fileLogger.info(NewsService.class + ": " + exception.getMessage());
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
            return List.of();
        }
    }

    public void hideNews(NewsIds newsIds) {
        toggleNewsHiddenStatus(ApiUrls.HIDE_NEWS, newsIds);
    }

    public void unHideNews(NewsIds newsIds) {
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
                    CommonUtility.getDefaultHeaders(),
                    Void.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Failed to unhide news: " + newsIds);
            }
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
        }
    }

    public List<com.intimetec.newsaggregation.client.dto.response.NewsResponse> getAllHiddenNews() {
        try {
            ApiResponse<List<com.intimetec.newsaggregation.client.dto.response.NewsResponse>> response = this.httpClient.getList(
                    ApiUrls.HIDDEN_NEWS,
                    CommonUtility.getDefaultHeaders(),
                    com.intimetec.newsaggregation.client.dto.response.NewsResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to get hidden news data")
            );
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
            return List.of();
        }
    }

    public Optional<com.intimetec.newsaggregation.dto.response.NewsResponse> getNewsById(Long newsId) {
        try {
            final String url = String.format(ApiUrls.NEWS_ID, newsId);
            ApiResponse<com.intimetec.newsaggregation.dto.response.NewsResponse> response = this.httpClient.get(
                    url,
                    CommonUtility.getDefaultHeaders(),
                    com.intimetec.newsaggregation.dto.response.NewsResponse.class
            );
            var newsResponse = CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Unable to fetch news by id")
            );
            return Optional.of(newsResponse);
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
            return Optional.empty();
        }
    }

    public void hideNewsByKeywords(Keywords keywords) {
        try {
            this.httpClient.put(
                    ApiUrls.HIDE_NEWS_BY_KEYWORDS,
                    keywords,
                    CommonUtility.getDefaultHeaders(),
                    Void.class
            );
        } catch (Exception exception) {
            fileLogger.error(NewsService.class + ": " + exception.getMessage());
        }
    }

}