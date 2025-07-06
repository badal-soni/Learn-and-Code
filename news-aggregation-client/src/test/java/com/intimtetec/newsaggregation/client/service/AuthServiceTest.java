package com.intimtetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.dto.request.UserSignInRequest;
import com.intimetec.newsaggregation.client.dto.request.UserSignUpRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.UserSignInResponse;
import com.intimetec.newsaggregation.client.dto.response.UserSignUpResponse;
import com.intimetec.newsaggregation.client.service.AuthService;
import com.intimetec.newsaggregation.client.util.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() throws Exception {
        authService = new AuthService();
        mockHttpClient = mock(HttpClient.class);

        setPrivateField(authService, "httpClient", mockHttpClient);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void testSignUpSuccess() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest();
        UserSignUpResponse expectedResponse = new UserSignUpResponse();
        ApiResponse<UserSignUpResponse> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedResponse);

        when(mockHttpClient.post(eq(ApiUrls.SIGN_UP), eq(request), anyMap(), eq(UserSignUpResponse.class)))
                .thenReturn(apiResponse);

        UserSignUpResponse actualResponse = authService.signUp(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.SIGN_UP), eq(request), anyMap(), eq(UserSignUpResponse.class));
    }

    @Test
    void testSignUpFailure() throws Exception {
        UserSignUpRequest request = new UserSignUpRequest();
        ApiResponse<UserSignUpResponse> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setData(null);

        when(mockHttpClient.post(eq(ApiUrls.SIGN_UP), eq(request), anyMap(), eq(UserSignUpResponse.class)))
                .thenReturn(apiResponse);

        UserSignUpResponse actualResponse = authService.signUp(request);

        assertNull(actualResponse);
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.SIGN_UP), eq(request), anyMap(), eq(UserSignUpResponse.class));
    }

    @Test
    void testSignInSuccess() throws Exception {
        UserSignInRequest request = new UserSignInRequest();
        UserSignInResponse expectedResponse = new UserSignInResponse();
        ApiResponse<UserSignInResponse> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(true);
        apiResponse.setData(expectedResponse);

        when(mockHttpClient.post(eq(ApiUrls.SIGN_IN), eq(request), anyMap(), eq(UserSignInResponse.class)))
                .thenReturn(apiResponse);

        authService.signIn(request);
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.SIGN_IN), eq(request), anyMap(), eq(UserSignInResponse.class));
    }

    @Test
    void testSignInFailure() throws Exception {
        UserSignInRequest request = new UserSignInRequest();
        ApiResponse<UserSignInResponse> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(false);
        apiResponse.setData(null);

        when(mockHttpClient.post(eq(ApiUrls.SIGN_IN), eq(request), anyMap(), eq(UserSignInResponse.class)))
                .thenReturn(apiResponse);

        UserSignInResponse actualResponse = authService.signIn(request);

        assertNull(actualResponse);
        verify(mockHttpClient, times(1)).post(eq(ApiUrls.SIGN_IN), eq(request), anyMap(), eq(UserSignInResponse.class));
    }
}