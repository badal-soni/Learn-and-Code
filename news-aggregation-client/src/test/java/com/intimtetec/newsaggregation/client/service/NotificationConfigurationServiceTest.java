package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.service.NotificationConfigurationService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationConfigurationServiceTest {

    private NotificationConfigurationService notificationConfigurationService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        notificationConfigurationService = new NotificationConfigurationService();
        mockHttpClient = mock(HttpClient.class);
        TestUtil.setPrivateField(notificationConfigurationService, "httpClient", mockHttpClient);
    }

    @Test
    void testGetAllNotificationConfigurationsSuccess() throws Exception {
        ApiResponse<AllNotificationConfigurations> apiResponse = new ApiResponse<>();
        AllNotificationConfigurations expectedConfigurations = new AllNotificationConfigurations();
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedConfigurations);

        when(mockHttpClient.get(eq(ApiUrls.NOTIFICATION_CONFIGURATIONS), anyMap(), eq(AllNotificationConfigurations.class)))
                .thenReturn(apiResponse);

        AllNotificationConfigurations actualConfigurations = notificationConfigurationService.getAllNotificationConfigurations();

        assertNotNull(actualConfigurations);
        assertEquals(expectedConfigurations, actualConfigurations);
        verify(mockHttpClient, times(1)).get(eq(ApiUrls.NOTIFICATION_CONFIGURATIONS), anyMap(), eq(AllNotificationConfigurations.class));
    }

    @Test
    void testUpdateNotificationPreferencesSuccess() throws Exception {
        UpdateNotificationPreferencesRequest request = new UpdateNotificationPreferencesRequest();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);

        when(mockHttpClient.put(eq(ApiUrls.NOTIFICATION_CONFIGURATIONS), eq(request), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> notificationConfigurationService.updateNotificationPreferences(request));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.NOTIFICATION_CONFIGURATIONS), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testGetAllKeywordsOfUserSuccess() throws Exception {
        ApiResponse<List<KeywordResponse>> apiResponse = new ApiResponse<>();
        List<KeywordResponse> expectedKeywords = List.of(new KeywordResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedKeywords);

        when(mockHttpClient.getList(eq(ApiUrls.KEYWORDS), anyMap(), eq(KeywordResponse.class)))
                .thenReturn(apiResponse);

        List<KeywordResponse> actualKeywords = notificationConfigurationService.getAllKeywordsOfUser();

        assertNotNull(actualKeywords);
        assertEquals(expectedKeywords, actualKeywords);
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.KEYWORDS), anyMap(), eq(KeywordResponse.class));
    }

    @Test
    void testToggleKeywordActiveStatusSuccess() throws Exception {
        Long keywordId = 1L;

        assertDoesNotThrow(() -> notificationConfigurationService.toggleKeywordActiveStatus(keywordId));
        verify(mockHttpClient, times(1)).put(eq(String.format(ApiUrls.TOGGLE_KEYWORD_ACTIVE_STATUS, keywordId)), eq(null), anyMap(), eq(Void.class));
    }

    @Test
    void testAddKeywordSuccess() throws Exception {
        CreateKeywordRequest request = new CreateKeywordRequest();

        assertDoesNotThrow(() -> notificationConfigurationService.addKeyword(request));
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.KEYWORDS), eq(request), anyMap(), eq(Void.class));
    }
}
