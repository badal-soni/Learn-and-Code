package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.service.NotificationService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.NotificationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    private NotificationService notificationService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        notificationService = new NotificationService();
        mockHttpClient = mock(HttpClient.class);
        TestUtil.setPrivateField(notificationService, "httpClient", mockHttpClient);
    }

    @Test
    void testGetAllNotificationsSuccess() throws Exception {
        ApiResponse<List<NotificationResponse>> apiResponse = new ApiResponse<>();
        List<NotificationResponse> expectedNotifications = List.of(new NotificationResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedNotifications);

        when(mockHttpClient.getList(eq(ApiUrls.NOTIFICATIONS), anyMap(), eq(NotificationResponse.class)))
                .thenReturn(apiResponse);

        List<NotificationResponse> actualNotifications = notificationService.getAllNotifications();

        assertNotNull(actualNotifications);
        assertEquals(expectedNotifications, actualNotifications);
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.NOTIFICATIONS), anyMap(), eq(NotificationResponse.class));
    }

    @Test
    void testGetAllNotificationsFailure() throws Exception {
        ApiResponse<List<NotificationResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setData(null);

        when(mockHttpClient.getList(eq(ApiUrls.NOTIFICATIONS), anyMap(), eq(NotificationResponse.class)))
                .thenReturn(apiResponse);

        List<NotificationResponse> actualNotifications = notificationService.getAllNotifications();

        assertNotNull(actualNotifications);
        assertTrue(actualNotifications.isEmpty());
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.NOTIFICATIONS), anyMap(), eq(NotificationResponse.class));
    }
}
