package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface NewsService {

    void toggleLikeStatus(Long newsId, User likedBy);

    List<NewsResponse> getHeadlines(User currentUser, ViewHeadlinesRequest viewHeadlinesRequest);

    List<NewsResponse> getHeadlinesUnderCategory(User currentUser, String categoryName, ViewHeadlinesRequest viewHeadlinesRequest);

    void saveNews(Long newsId, User savedBy);

    List<NewsResponse> searchNews(SearchNewsRequest searchNewsRequest);

    void unSaveNews(Long newsId, User savedBy);

    List<NewsResponse> getTodayHeadline(User currentUser);

    List<NewsResponse> getHiddenNews();

    List<NewsResponse> getNewsWithKeywords(Keywords keywords);

    NewsResponse getNewsById(Long newsId, User user);

}
