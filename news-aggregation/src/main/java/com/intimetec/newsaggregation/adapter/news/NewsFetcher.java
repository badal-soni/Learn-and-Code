package com.intimetec.newsaggregation.adapter.news;

import com.intimetec.newsaggregation.entity.News;

import java.util.List;

public interface NewsFetcher {

    List<News> fetchNews();

    String getServiceName();

}
