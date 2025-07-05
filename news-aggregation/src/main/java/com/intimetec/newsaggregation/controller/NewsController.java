package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.constant.Constants;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
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
import org.springframework.security.access.annotation.Secured;
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
    public ResponseEntity<ApiSuccessResponse> viewHeadlines(
            @CurrentUser User currentUser,
            @ModelAttribute ViewHeadlinesRequest viewHeadlinesRequest
    ) {
        return HttpUtil.sendResponseWithData(
                newsService.getHeadlines(currentUser, viewHeadlinesRequest),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/headlines/today")
    public ResponseEntity<ApiSuccessResponse> viewTodayHeadlines(@CurrentUser User currentUser) {
        return HttpUtil.sendResponseWithData(
                newsService.getTodayHeadline(currentUser),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{categoryName}/headlines")
    public ResponseEntity<ApiSuccessResponse> viewHeadlinesUnderCategory(
            @CurrentUser User currentUser,
            @PathVariable String categoryName,
            @ModelAttribute ViewHeadlinesRequest viewHeadlinesRequest
    ) {
        return HttpUtil.sendResponseWithData(
                newsService.getHeadlinesUnderCategory(currentUser, categoryName, viewHeadlinesRequest),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/{newsId}/toggle-like")
    public ResponseEntity<ApiSuccessResponse> toggleNewsLike(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.toggleLikeStatus(newsId, currentUser);
        return HttpUtil.sendResponseWithNoData(HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/search",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> searchNews(@RequestBody SearchNewsRequest searchNewsRequest) {
        return HttpUtil.sendResponseWithData(
                newsService.searchNews(searchNewsRequest),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/{newsId}/save")
    public ResponseEntity<ApiSuccessResponse> saveNewsArticle(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.saveNews(newsId, currentUser);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @DeleteMapping(path = "/{newsId}/unsave")
    public ResponseEntity<ApiSuccessResponse> unSaveNewsArticle(
            @PathVariable Long newsId,
            @CurrentUser User currentUser
    ) {
        newsService.unSaveNews(newsId, currentUser);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
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
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @GetMapping(path = "/reported")
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> viewAllReportedNews() {
        return HttpUtil.sendResponseWithData(
                reportNewsService.getAllReportedNews(),
                HttpStatus.OK
        );
    }

    @PutMapping(
            path = "/hide",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> hideNewsArticle(@RequestBody NewsIds newsIds) {
        reportNewsService.hideNews(newsIds);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @PutMapping(
            path = "/unhide",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> unHideNewsArticle(@RequestBody NewsIds newsIds) {
        reportNewsService.unHideNews(newsIds);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @PutMapping(
            path = "/hide/keywords",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> hideByKeywords(@RequestBody Keywords keywords) {
        reportNewsService.hideByKeywords(keywords);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @GetMapping(path = "/hidden")
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> viewAllHiddenNews() {
        return HttpUtil.sendResponseWithData(
                newsService.getHiddenNews(),
                HttpStatus.OK
        );
    }

    @PostMapping(
            path = "/keywords",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Secured(value = Constants.SPRING_ROLE_ADMIN)
    public ResponseEntity<ApiSuccessResponse> viewNewsWithKeywords(@RequestBody Keywords keywords) {
        return HttpUtil.sendResponseWithData(
                newsService.getNewsWithKeywords(keywords),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{newsId}")
    public ResponseEntity<ApiSuccessResponse> viewNewsById(
            @PathVariable Long newsId,
            @CurrentUser User user
    ) {
        return HttpUtil.sendResponseWithData(
                newsService.getNewsById(newsId, user),
                HttpStatus.OK
        );
    }

}
