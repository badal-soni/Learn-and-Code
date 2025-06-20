package com.intimetec.newsaggregation.client.service;

import com.intimetec.newsaggregation.client.constant.Api;
import com.intimetec.newsaggregation.client.constant.HttpHeader;
import com.intimetec.newsaggregation.client.constant.MediaType;
import com.intimetec.newsaggregation.client.constant.UserState;
import com.intimetec.newsaggregation.client.dto.request.UserSignInRequest;
import com.intimetec.newsaggregation.client.dto.request.UserSignUpRequest;
import com.intimetec.newsaggregation.client.dto.response.ApiResponse;
import com.intimetec.newsaggregation.client.dto.response.UserSignInResponse;
import com.intimetec.newsaggregation.client.dto.response.UserSignUpResponse;
import com.intimetec.newsaggregation.client.util.HttpClient;
import com.intimetec.newsaggregation.client.util.Logger;

import java.util.Map;

public class AuthService {

    private final HttpClient httpClient;
    private final Logger logger;

    public AuthService() {
        this.httpClient = new HttpClient();
        this.logger = new Logger();
    }

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        try {
            ApiResponse<UserSignUpResponse> response = httpClient.post(
                    Api.SIGN_UP,
                    userSignUpRequest,
                    Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    UserSignUpResponse.class
            );
            logger.info("Response: " + response);
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
            return response.getData();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
        return null;
    }

    public UserSignInResponse signIn(UserSignInRequest userSignInRequest) {
        try {
            ApiResponse<UserSignInResponse> response = httpClient.post(
                    Api.SIGN_IN,
                    userSignInRequest,
                    Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON),
                    UserSignInResponse.class
            );
            logger.info("Response: " + response);
            if (!response.isSuccess()) {
                throw new Exception("Got error: " + response.getData());
            }
            UserState.isLoggedIn = true;
            UserState.userRole = response.getData().getRoles().stream().findFirst().get();
            return response.getData();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
        return null;
    }

}
