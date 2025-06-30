package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = ApiVersions.V1_KEYWORDS,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class KeywordsController {

    private final KeywordService keywordService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse> createKeyword(
            @RequestBody CreateKeywordRequest createKeywordRequest,
            @CurrentUser User currentUser
    ) {
        keywordService.createKeyword(createKeywordRequest, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PutMapping(path = "/{keywordId}/status/toggle")
    public ResponseEntity<ApiSuccessResponse> toggleActivateStatus(
            @PathVariable Long keywordId,
            @CurrentUser User currentUser
    ) {
        keywordService.toggleKeywordActiveStatus(keywordId, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse> viewAllKeywords(@CurrentUser User currentUser) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(keywordService.getAllKeywordsOfUser(currentUser))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
