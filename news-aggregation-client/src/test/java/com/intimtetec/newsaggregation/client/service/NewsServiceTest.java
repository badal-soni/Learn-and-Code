package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.NewsResponse;
import com.intimetec.newsaggregation.client.service.NewsService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsServiceTest {

    private NewsService newsService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        newsService = new NewsService();
        mockHttpClient = mock(HttpClient.class);
        TestUtil.setPrivateField(newsService, "httpClient", mockHttpClient);
    }

    @Test
    void testSaveNews() throws Exception {
        long newsId = 1L;
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);

        when(mockHttpClient.post(eq(String.format(ApiUrls.SAVE_NEWS, newsId)), eq(null), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> newsService.saveNews(newsId));
        verify(mockHttpClient, times(1)).post(eq(String.format(ApiUrls.SAVE_NEWS, newsId)), eq(null), anyMap(), eq(Void.class));
    }

    @Test
    void testUnSaveNews() throws Exception {
        long newsId = 1L;

        assertDoesNotThrow(() -> newsService.unSaveNews(newsId));
        verify(mockHttpClient, times(1)).delete(eq(String.format(ApiUrls.UNSAVE_NEWS, newsId)), anyMap(), eq(null));
    }

    @Test
    void testToggleNewsLike() throws Exception {
        long newsId = 1L;
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);

        when(mockHttpClient.post(eq(String.format(ApiUrls.TOGGLE_LIKE, newsId)), eq(null), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> newsService.toggleNewsLike(newsId));
        verify(mockHttpClient, times(1)).post(eq(String.format(ApiUrls.TOGGLE_LIKE, newsId)), eq(null), anyMap(), eq(Void.class));
    }

    @Test
    void testViewTodaysNews() throws Exception {
        ApiResponse<List<NewsResponse>> apiResponse = new ApiResponse<>();
        List<NewsResponse> expectedNews = List.of(new NewsResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedNews);

        when(mockHttpClient.getList(eq(ApiUrls.TODAY_NEWS), anyMap(), eq(NewsResponse.class)))
                .thenReturn(apiResponse);

        List<NewsResponse> actualNews = newsService.viewTodaysNews();

        assertNotNull(actualNews);
        assertEquals(expectedNews, actualNews);
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.TODAY_NEWS), anyMap(), eq(NewsResponse.class));
    }

    @Test
    void testGetAllNewsUnderCategoryBetweenDates() throws Exception {
        String category = "Sports";
        LocalDate fromDate = LocalDate.now().minusDays(7);
        LocalDate toDate = LocalDate.now();
        String url = String.format(ApiUrls.CATEGORY_HEADLINES, category, fromDate, toDate);

        ApiResponse<List<NewsResponse>> apiResponse = new ApiResponse<>();
        List<NewsResponse> expectedNews = List.of(new NewsResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedNews);

        when(mockHttpClient.getList(eq(url), anyMap(), eq(NewsResponse.class)))
                .thenReturn(apiResponse);

        List<NewsResponse> actualNews = newsService.getAllNewsUnderCategoryBetweenDates(category, fromDate, toDate);

        assertNotNull(actualNews);
        assertEquals(expectedNews, actualNews);
        verify(mockHttpClient, times(1)).getList(eq(url), anyMap(), eq(NewsResponse.class));
    }

    @Test
    void testSearchNews() throws Exception {
        SearchNewsRequest request = new SearchNewsRequest();
        ApiResponse<List<com.intimetec.newsaggregation.dto.response.NewsResponse>> apiResponse = new ApiResponse<>();
        List<com.intimetec.newsaggregation.dto.response.NewsResponse> expectedNews = List.of(new com.intimetec.newsaggregation.dto.response.NewsResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedNews);

        when(mockHttpClient.postList(eq(ApiUrls.SEARCH_NEWS), eq(request), anyMap(), eq(com.intimetec.newsaggregation.dto.response.NewsResponse.class)))
                .thenReturn(apiResponse);

        List<com.intimetec.newsaggregation.client.dto.response.NewsResponse> actualNews = newsService.searchNews(request);

        assertNotNull(actualNews);
    }

    @Test
    void testReportNews() throws Exception {
        Long newsId = 1L;
        ReportNewsArticleRequest request = new ReportNewsArticleRequest();

        assertDoesNotThrow(() -> newsService.reportNews(newsId, request));
        verify(mockHttpClient, times(1)).post(eq(String.format(ApiUrls.REPORT_NEWS, newsId)), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testGetAllReportedNews() throws Exception {
        ApiResponse<List<ReportedNewsResponse>> apiResponse = new ApiResponse<>();
        List<ReportedNewsResponse> expectedNews = List.of(new ReportedNewsResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedNews);

        when(mockHttpClient.getList(eq(ApiUrls.REPORTED_NEWS), anyMap(), eq(ReportedNewsResponse.class)))
                .thenReturn(apiResponse);

        List<ReportedNewsResponse> actualNews = newsService.getAllReportedNews();

        assertNotNull(actualNews);
        assertEquals(expectedNews, actualNews);
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.REPORTED_NEWS), anyMap(), eq(ReportedNewsResponse.class));
    }

    @Test
    void testHideNews() throws Exception {
        NewsIds newsIds = new NewsIds();

        assertDoesNotThrow(() -> newsService.hideNews(newsIds));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.HIDE_NEWS), eq(newsIds), anyMap(), eq(Void.class));
    }

    @Test
    void testUnHideNews() throws Exception {
        NewsIds newsIds = new NewsIds();

        assertDoesNotThrow(() -> newsService.unHideNews(newsIds));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.UNHIDE_NEWS), eq(newsIds), anyMap(), eq(Void.class));
    }
}
