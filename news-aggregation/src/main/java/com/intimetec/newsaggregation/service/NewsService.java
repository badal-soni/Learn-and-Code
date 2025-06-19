package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.DateRangeFilterCriteria;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface NewsService {

    void likeNews(Long newsId, User likedBy);

    void dislikeNews(Long newsId, User dislikedBy);

    List<NewsResponse> getHeadlines(ViewHeadlinesRequest viewHeadlinesRequest);

    List<NewsResponse> getHeadlinesUnderCategory(String categoryName, ViewHeadlinesRequest viewHeadlinesRequest);

    void saveNews(Long newsId, User savedBy);

    List<NewsResponse> searchNews(DateRangeFilterCriteria dateRangeFilterCriteria);

    void unSaveNews(Long newsId, User savedBy);

}
