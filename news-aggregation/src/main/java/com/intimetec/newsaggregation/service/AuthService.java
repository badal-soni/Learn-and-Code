package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.SignInRequest;
import com.intimetec.newsaggregation.dto.request.SignUpRequest;
import com.intimetec.newsaggregation.dto.response.SignInResponse;
import com.intimetec.newsaggregation.dto.response.SignUpResponse;

public interface AuthService {

    SignUpResponse signUpUser(SignUpRequest signUpRequest);

    SignInResponse signInUser(SignInRequest signInRequest);

}
