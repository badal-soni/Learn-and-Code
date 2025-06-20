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
        consumes = MediaType.APPLICATION_JSON_VALUE,
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

    @PatchMapping(path = "/{id}/activate")
    public ResponseEntity<ApiSuccessResponse> activateKeyword(
            @PathVariable Long id,
            @CurrentUser User currentUser
    ) {
        keywordService.activateKeyword(id, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PatchMapping(path = "/{id}/deactivate")
    public ResponseEntity<ApiSuccessResponse> deactivateKeyword(
            @PathVariable Long id,
            @CurrentUser User currentUser
    ) {
        keywordService.deactivateKeyword(id, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

}
