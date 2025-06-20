package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.request.DateRangeFilterCriteria;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = ApiVersions.V1_NEWS,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping(path = "/headlines")
    public ResponseEntity<ApiSuccessResponse> viewHeadlines(@ModelAttribute ViewHeadlinesRequest viewHeadlinesRequest) {
        System.out.println("FullAccess controller invoked");
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getHeadlines(viewHeadlinesRequest))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/{categoryName}/headlines")
    public ResponseEntity<ApiSuccessResponse> viewHeadlinesUnderCategory(
            @PathVariable String categoryName,
            @ModelAttribute ViewHeadlinesRequest viewHeadlinesRequest
    ) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getHeadlinesUnderCategory(categoryName, viewHeadlinesRequest))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping(
            path = "/{newsId}/like",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> likeNews(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.likeNews(newsId, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(path = "/{newsId}/dislike")
    public ResponseEntity<ApiSuccessResponse> dislikeNews(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.dislikeNews(newsId, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PostMapping(
            path = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> searchNews(@RequestBody DateRangeFilterCriteria dateRangeFilterCriteria) {
        return ApiSuccessResponse.builder()
                .data(newsService.searchNews(dateRangeFilterCriteria))
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping(path = "/{newsId}/save")
    public ResponseEntity<ApiSuccessResponse> saveNewsArticle(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.saveNews(newsId, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping(path = "/{newsId}/unsave")
    public ResponseEntity<ApiSuccessResponse> unSaveNewsArticle(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.unSaveNews(newsId, currentUser);
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
