package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.ApiUrls;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.context.UserContextHolder;
import com.intimetec.newsaggregation.client.dto.request.UserSignInRequest;
import com.intimetec.newsaggregation.client.dto.request.UserSignUpRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.UserSignInResponse;
import com.intimetec.newsaggregation.client.dto.response.UserSignUpResponse;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.client.util.ConsoleLogger;

import java.util.Map;

public class AuthService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;

    public AuthService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
    }

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        try {
            ApiResponse<UserSignUpResponse> response = httpClient.post(
                    ApiUrls.SIGN_UP,
                    userSignUpRequest,
                    Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    UserSignUpResponse.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
            return response.getData();
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return null;
    }

    public UserSignInResponse signIn(UserSignInRequest userSignInRequest) {
        try {
            ApiResponse<UserSignInResponse> response = httpClient.post(
                    ApiUrls.SIGN_IN,
                    userSignInRequest,
                    Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    UserSignInResponse.class
            );
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
            UserContextHolder.isLoggedIn = true;
            UserContextHolder.userRole = response.getData().getRoles().stream().findFirst().get();
            return response.getData();
        } catch (Exception exception) {
            consoleLogger.error(exception.getMessage());
        }
        return null;
    }

}
