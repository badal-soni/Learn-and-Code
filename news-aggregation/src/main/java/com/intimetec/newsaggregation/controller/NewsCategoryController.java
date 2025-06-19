package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.KeywordService;
import com.intimetec.newsaggregation.service.NewsCategoryService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiVersions.V1_CATEGORIES)
@RequiredArgsConstructor
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;
    private final KeywordService keywordService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        newsCategoryService.createCategory(createCategoryRequest);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> viewAllCategories() {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .data(newsCategoryService.listAllCategories())
                .build();
    }

    @PostMapping(
            path = "/{categoryName}/keywords/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> addKeywordUnderCategory(
            @PathVariable @Pattern(regexp = Constants.LOWERCASE_REGULAR_EXPRESSION) String categoryName,
            @RequestBody CreateKeywordRequest createKeywordRequest,
            @CurrentUser User user
    ) {
        createKeywordRequest.setParentCategory(categoryName);
        keywordService.createKeyword(createKeywordRequest, user);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

}
