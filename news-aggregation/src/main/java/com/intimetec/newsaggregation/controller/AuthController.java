package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.dto.request.SignInRequest;
import com.intimetec.newsaggregation.dto.request.SignUpRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.service.AuthService;
import com.intimetec.newsaggregation.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = ApiVersions.V1_AUTH,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            path = "/sign-up",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return HttpUtil.sendResponseWithData(
                authService.signUpUser(signUpRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping(
            path = "/sign-in",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return HttpUtil.sendResponseWithData(
                authService.signInUser(signInRequest),
                HttpStatus.OK
        );
    }

}
