package com.intimetec.newsaggregation.adapter;

import com.intimetec.newsaggregation.constant.PredefinedCategories;
import com.intimetec.newsaggregation.entity.News;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@FunctionalInterface
public interface NewsFetcher {

    List<News> fetchNews();

    default String assignCategory(@NotNull News news) {
        String text = (news.getHeadline() + " " + news.getDescription()).toLowerCase();
        if (text.contains("sport") || text.contains("football")) return PredefinedCategories.SPORTS;
        if (text.contains("business") || text.contains("market")) return PredefinedCategories.BUSINESS;
        if (text.contains("movie") || text.contains("music")) return PredefinedCategories.ENTERTAINMENT;
        if (text.contains("tech") || text.contains("ai")) return PredefinedCategories.TECHNOLOGY;
        return PredefinedCategories.GENERAL;
    }

}
