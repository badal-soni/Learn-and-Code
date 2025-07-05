package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface ReportNewsService {

    void reportNews(Long newsId, ReportNewsArticleRequest reportNewsArticleRequest, User reportedBy);

    void hideNews(NewsIds newsIds);

    void unHideNews(NewsIds newsIds);

    void hideByKeywords(Keywords keywords);

    List<ReportedNewsResponse> getAllReportedNews();

}
