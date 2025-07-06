package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface NewsPreferencesService {

    List<NewsResponse> sortNewsByUserPreferences(User user, List<News> newsList);

    List<NewsResponse> sortCategoryNews(User user, List<News> categoryNewsList);

}
