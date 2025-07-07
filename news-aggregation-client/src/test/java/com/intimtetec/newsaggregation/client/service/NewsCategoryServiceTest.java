package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.service.NewsCategoryService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.NewsCategories;
import com.intimetec.newsaggregation.dto.response.NewsCategoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NewsCategoryServiceTest {

    private NewsCategoryService newsCategoryService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        newsCategoryService = new NewsCategoryService();
        mockHttpClient = mock(HttpClient.class);
        TestUtil.setPrivateField(newsCategoryService, "httpClient", mockHttpClient);
    }

    @Test
    void testAddCategorySuccess() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);

        when(mockHttpClient.post(eq(ApiUrls.CATEGORIES), eq(request), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> newsCategoryService.addCategory(request));
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.CATEGORIES), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testGetAllCategoriesSuccess() throws Exception {
        ApiResponse<NewsCategoryResponse> apiResponse = new ApiResponse<>();
        NewsCategoryResponse expectedResponse = new NewsCategoryResponse();
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedResponse);

        when(mockHttpClient.get(eq(ApiUrls.UNHIDDEN_CATEGORIES), anyMap(), eq(NewsCategoryResponse.class)))
                .thenReturn(apiResponse);

        Optional<NewsCategoryResponse> actualResponse = newsCategoryService.getAllCategories();

        assertTrue(actualResponse.isPresent());
        assertEquals(expectedResponse, actualResponse.get());
        verify(mockHttpClient, times(1)).get(eq(ApiUrls.UNHIDDEN_CATEGORIES), anyMap(), eq(NewsCategoryResponse.class));
    }

    @Test
    void testHideNewsCategories() throws Exception {
        List<String> categories = List.of("Category1", "Category2");
        NewsCategories request = new NewsCategories();
        request.setNewsCategories(categories);

        assertDoesNotThrow(() -> newsCategoryService.hideNewsCategories(categories));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.HIDE_CATEGORY), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testUnHideNewsCategories() throws Exception {
        List<String> categories = List.of("Category1", "Category2");
        NewsCategories request = new NewsCategories();
        request.setNewsCategories(categories);

        assertDoesNotThrow(() -> newsCategoryService.unHideNewsCategories(categories));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.UNHIDE_CATEGORY), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testGetAllHiddenNewsCategoriesSuccess() throws Exception {
        ApiResponse<NewsCategoryResponse> apiResponse = new ApiResponse<>();
        NewsCategoryResponse expectedResponse = new NewsCategoryResponse();
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedResponse);

        when(mockHttpClient.get(eq(ApiUrls.HIDDEN_CATEGORIES), anyMap(), eq(NewsCategoryResponse.class)))
                .thenReturn(apiResponse);

        Optional<NewsCategoryResponse> actualResponse = newsCategoryService.getAllHiddenNewsCategories();

        assertTrue(actualResponse.isPresent());
        assertEquals(expectedResponse, actualResponse.get());
        verify(mockHttpClient, times(1)).get(eq(ApiUrls.HIDDEN_CATEGORIES), anyMap(), eq(NewsCategoryResponse.class));
    }

    @Test
    void testGetAllUnHiddenNewsCategoriesSuccess() throws Exception {
        ApiResponse<NewsCategoryResponse> apiResponse = new ApiResponse<>();
        NewsCategoryResponse expectedResponse = new NewsCategoryResponse();
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedResponse);

        when(mockHttpClient.get(eq(ApiUrls.UNHIDDEN_CATEGORIES), anyMap(), eq(NewsCategoryResponse.class)))
                .thenReturn(apiResponse);

        Optional<NewsCategoryResponse> actualResponse = newsCategoryService.getAllUnHiddenNewsCategories();

        assertTrue(actualResponse.isPresent());
        assertEquals(expectedResponse, actualResponse.get());
        verify(mockHttpClient, times(1)).get(eq(ApiUrls.UNHIDDEN_CATEGORIES), anyMap(), eq(NewsCategoryResponse.class));
    }
}
