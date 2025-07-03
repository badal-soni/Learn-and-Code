package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.KeywordService;
import com.intimetec.newsaggregation.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @PutMapping(path = "/{keywordId}/status/toggle")
    public ResponseEntity<ApiSuccessResponse> toggleActivateStatus(
            @PathVariable Long keywordId,
            @CurrentUser User currentUser
    ) {
        keywordService.toggleKeywordActiveStatus(keywordId, currentUser);
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse> getAllKeywordsOfUser(@CurrentUser User currentUser) {
        return HttpUtil.sendResponseWithData(
                keywordService.getAllKeywordsOfUser(currentUser),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/blocked/{keyword}")
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> addBlockedKeyword(
            @PathVariable String keyword
    ) {
        keywordService.addBlockedKeyword(keyword);
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @GetMapping(path = "/blocked")
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> getAllBlockedKeywords() {
        return HttpUtil.sendResponseWithData(
                keywordService.getAllBlockedKeywords(),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/blocked/{keyword}")
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> removeBlockedKeyword(
            @PathVariable String keyword
    ) {
        keywordService.deleteBlockedKeyword(keyword);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

}
