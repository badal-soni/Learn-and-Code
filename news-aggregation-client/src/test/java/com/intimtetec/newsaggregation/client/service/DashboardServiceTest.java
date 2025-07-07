package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.service.DashboardService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceTest {

    private DashboardService dashboardService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        dashboardService = new DashboardService();
        mockHttpClient = mock(HttpClient.class);
        TestUtil.setPrivateField(dashboardService, "httpClient", mockHttpClient);
    }

    @Test
    void testGetSavedNewsSuccess() throws Exception {
        ApiResponse<List<SavedNewsResponse>> apiResponse = new ApiResponse<>();
        List<SavedNewsResponse> expectedSavedNews = List.of(new SavedNewsResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedSavedNews);

        when(mockHttpClient.getList(eq(ApiUrls.SAVED_NEWS), anyMap(), eq(SavedNewsResponse.class)))
                .thenReturn(apiResponse);

        List<SavedNewsResponse> actualSavedNews = dashboardService.getSavedNews();

        assertNotNull(actualSavedNews);
        assertEquals(expectedSavedNews, actualSavedNews);
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.SAVED_NEWS), anyMap(), eq(SavedNewsResponse.class));
    }

    @Test
    void testGetSavedNewsFailure() throws Exception {
        ApiResponse<List<SavedNewsResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setData(null);

        when(mockHttpClient.getList(eq(ApiUrls.SAVED_NEWS), anyMap(), eq(SavedNewsResponse.class)))
                .thenReturn(apiResponse);

        List<SavedNewsResponse> actualSavedNews = dashboardService.getSavedNews();

        assertNotNull(actualSavedNews);
        assertTrue(actualSavedNews.isEmpty());
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.SAVED_NEWS), anyMap(), eq(SavedNewsResponse.class));
    }
}