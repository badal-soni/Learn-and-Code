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
import com.intimetec.newsaggregation.client.logger.ConsoleLogger;
import com.intimetec.newsaggregation.client.logger.FileLogger;
import com.intimetec.newsaggregation.client.util.CommonUtility;
import com.intimetec.newsaggregation.client.util.HttpClient;

import java.util.Map;

public class AuthService {

    private final HttpClient httpClient;
    private final ConsoleLogger consoleLogger;
    private final FileLogger fileLogger;

    public AuthService() {
        this.httpClient = new HttpClient();
        this.consoleLogger = new ConsoleLogger();
        this.fileLogger = FileLogger.getInstance();
    }

    public UserSignUpResponse signUp(UserSignUpRequest userSignUpRequest) {
        try {
            ApiResponse<UserSignUpResponse> response = httpClient.post(
                    ApiUrls.SIGN_UP,
                    userSignUpRequest,
                    CommonUtility.getDefaultHeaders(),
                    UserSignUpResponse.class
            );
            return CommonUtility.getDataOrElseThrow(
                    response,
                    new Exception("Got error: " + response.getData())
            );
        } catch (Exception exception) {
            fileLogger.error(AuthService.class + ": " + exception.getMessage());
        }
        return null;
    }

    public UserSignInResponse signIn(UserSignInRequest userSignInRequest) {
        try {
            final Map<String, String> headers = Map.of(HttpHeader.CONTENT_TYPE, MediaType.APPLICATION_JSON);
            ApiResponse<UserSignInResponse> response = httpClient.post(
                    ApiUrls.SIGN_IN,
                    userSignInRequest,
                    headers,
                    UserSignInResponse.class
            );
            if (!response.isSuccess()) {
                consoleLogger.info(response.getMessage());
                throw new Exception("Got error: " + response.getData());
            }
            UserContextHolder.isLoggedIn = true;
            UserContextHolder.userRole = response.getData().getRoles().stream().findFirst().get();
            UserContextHolder.accessToken = response.getData().getJwtToken();
            return response.getData();
        } catch (Exception exception) {
            fileLogger.error(AuthService.class + ": " + exception.getMessage());
        }
        return null;
    }

}
