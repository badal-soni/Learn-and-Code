package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.request.CreateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.request.UpdateExternalServerRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.service.ExternalServerDetailService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.dto.response.ExternalServerStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExternalServerDetailServiceTest {

    private ExternalServerDetailService externalServerDetailService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        externalServerDetailService = new ExternalServerDetailService();
        mockHttpClient = mock(HttpClient.class);

        setPrivateField(externalServerDetailService, "httpClient", mockHttpClient);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testRegisterExternalServerSuccess() throws Exception {
        CreateExternalServerRequest request = new CreateExternalServerRequest();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);

        when(mockHttpClient.post(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> externalServerDetailService.registerExternalServer(request));
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testRegisterExternalServerFailure() throws Exception {
        CreateExternalServerRequest request = new CreateExternalServerRequest();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);

        when(mockHttpClient.post(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> externalServerDetailService.registerExternalServer(request));
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testGetExternalServerStatusSuccess() throws Exception {
        ApiResponse<List<ExternalServerStatusResponse>> apiResponse = new ApiResponse<>();
        List<ExternalServerStatusResponse> expectedResponse = List.of(new ExternalServerStatusResponse());
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedResponse);

        when(mockHttpClient.getList(eq(ApiUrls.EXTERNAL_SERVER), anyMap(), eq(ExternalServerStatusResponse.class)))
                .thenReturn(apiResponse);

        List<ExternalServerStatusResponse> actualResponse = externalServerDetailService.getExternalServerStatus();

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.EXTERNAL_SERVER), anyMap(), eq(ExternalServerStatusResponse.class));
    }

    @Test
    void testGetExternalServerStatusFailure() throws Exception {
        ApiResponse<List<ExternalServerStatusResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setData(null);

        when(mockHttpClient.getList(eq(ApiUrls.EXTERNAL_SERVER), anyMap(), eq(ExternalServerStatusResponse.class)))
                .thenReturn(apiResponse);

        List<ExternalServerStatusResponse> actualResponse = externalServerDetailService.getExternalServerStatus();

        assertNotNull(actualResponse);
        assertTrue(actualResponse.isEmpty());
        verify(mockHttpClient, times(1)).getList(eq(ApiUrls.EXTERNAL_SERVER), anyMap(), eq(ExternalServerStatusResponse.class));
    }

    @Test
    void testUpdateExternalServerSuccess() throws Exception {
        UpdateExternalServerRequest request = new UpdateExternalServerRequest();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);

        when(mockHttpClient.put(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> externalServerDetailService.updateExternalServer(request));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class));
    }

    @Test
    void testUpdateExternalServerFailure() throws Exception {
        UpdateExternalServerRequest request = new UpdateExternalServerRequest();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);

        when(mockHttpClient.put(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class)))
                .thenReturn(apiResponse);

        assertDoesNotThrow(() -> externalServerDetailService.updateExternalServer(request));
        verify(mockHttpClient, times(1)).put(eq(ApiUrls.EXTERNAL_SERVER), eq(request), anyMap(), eq(Void.class));
    }
}