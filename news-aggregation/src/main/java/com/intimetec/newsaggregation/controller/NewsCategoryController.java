package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.NewsCategories;
import com.intimetec.newsaggregation.dto.request.CreateCategoryRequest;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.KeywordService;
import com.intimetec.newsaggregation.service.NewsCategoryService;
import com.intimetec.newsaggregation.util.HttpUtil;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = ApiVersions.V1_CATEGORIES,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class NewsCategoryController {

    private final NewsCategoryService newsCategoryService;
    private final KeywordService keywordService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Secured(Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        newsCategoryService.createCategory(createCategoryRequest);
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @GetMapping
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> viewAllCategories() {
        return HttpUtil.sendResponseWithData(
                newsCategoryService.getAllCategories(),
                HttpStatus.OK
        );
    }

    @PostMapping(
            path = "/{categoryName}/keywords",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> addKeywordUnderCategory(
            @PathVariable @Pattern(regexp = Constants.LOWERCASE_REGULAR_EXPRESSION) String categoryName,
            @RequestBody CreateKeywordRequest createKeywordRequest,
            @CurrentUser User user
    ) {
        createKeywordRequest.setParentCategory(categoryName);
        keywordService.createKeyword(createKeywordRequest, user);
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @PutMapping(
            path = "/hide",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> hideNewsCategory(@RequestBody NewsCategories newsCategories) {
        newsCategoryService.hideNewsCategories(newsCategories.getNewsCategories());
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @PutMapping(
            path = "/unhide",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> unHideNewsCategory(@RequestBody NewsCategories newsCategories) {
        newsCategoryService.unHideNewsCategories(newsCategories.getNewsCategories());
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @GetMapping(path = "/hidden")
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> viewHiddenNewsCategories() {
        return HttpUtil.sendResponseWithData(
                newsCategoryService.getAllHiddenCategories(),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/unhidden")
    public ResponseEntity<ApiSuccessResponse> viewUnHiddenNewsCategories() {
        return HttpUtil.sendResponseWithData(
                newsCategoryService.getAllUnHiddenCategories(),
                HttpStatus.OK
        );
    }

}
