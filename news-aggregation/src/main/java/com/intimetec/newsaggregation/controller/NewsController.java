package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.NewsService;
import com.intimetec.newsaggregation.service.ReportNewsService;
import com.intimetec.newsaggregation.util.HttpUtil;
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
    private final ReportNewsService reportNewsService;

    @GetMapping(path = "/headlines")
    public ResponseEntity<ApiSuccessResponse> viewHeadlines(@ModelAttribute ViewHeadlinesRequest viewHeadlinesRequest) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getHeadlines(viewHeadlinesRequest))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/headlines/today")
    public ResponseEntity<ApiSuccessResponse> viewTodayHeadlines() {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getTodayHeadline())
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

    @PostMapping(path = "/{newsId}/toggle-like")
    public ResponseEntity<ApiSuccessResponse> toggleNewsLike(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.toggleLikeStatus(newsId, currentUser);
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
    public ResponseEntity<ApiSuccessResponse> searchNews(@RequestBody SearchNewsRequest searchNewsRequest) {
        return ApiSuccessResponse.builder()
                .data(newsService.searchNews(searchNewsRequest))
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
        return HttpUtil.noBodyOkResponse();
    }

    @DeleteMapping(path = "/{newsId}/unsave")
    public ResponseEntity<ApiSuccessResponse> unSaveNewsArticle(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.unSaveNews(newsId, currentUser);
        return HttpUtil.noBodyOkResponse();
    }

    @PostMapping(
            path = "/{newsId}/report",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> reportNewsArticle(
            @PathVariable Long newsId,
            @RequestBody ReportNewsArticleRequest reportNewsArticleRequest,
            @CurrentUser User reportedBy
    ) {
        reportNewsService.reportNews(newsId, reportNewsArticleRequest, reportedBy);
        return HttpUtil.noBodyOkResponse();
    }

    @GetMapping(path = "/reported")
    public ResponseEntity<ApiSuccessResponse> viewAllReportedNews() {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(reportNewsService.getAllReportedNews())
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PutMapping(
            path = "/hide",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> hideNewsArticle(@RequestBody NewsIds newsIds) {
        reportNewsService.hideNews(newsIds);
        return HttpUtil.noBodyOkResponse();
    }

    @PutMapping(
            path = "/unhide",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> unHideNewsArticle(@RequestBody NewsIds newsIds) {
        reportNewsService.unHideNews(newsIds);
        return HttpUtil.noBodyOkResponse();
    }

    @PutMapping(
            path = "/hide/keywords",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> hideByKeywords(@RequestBody Keywords keywords) {
        reportNewsService.hideByKeywords(keywords);
        return HttpUtil.noBodyOkResponse();
    }

    @GetMapping(path = "/hidden")
    public ResponseEntity<ApiSuccessResponse> viewAllHiddenNews() {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getHiddenNews())
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @PostMapping(
            path = "/keywords",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> viewNewsWithKeywords(@RequestBody Keywords keywords) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getNewsWithKeywords(keywords))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/liked")
    public ResponseEntity<ApiSuccessResponse> viewAllNewsWithUserLikedStatus(@CurrentUser User user) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getAllNewsWithUserLikedStatus(user))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/{newsId}")
    public ResponseEntity<ApiSuccessResponse> viewNewsById(
            @PathVariable Long newsId,
            @CurrentUser User user
    ) {
        return ApiSuccessResponse.builder()
                .success(Constants.SUCCESS_TRUE)
                .data(newsService.getNewsById(newsId, user))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
