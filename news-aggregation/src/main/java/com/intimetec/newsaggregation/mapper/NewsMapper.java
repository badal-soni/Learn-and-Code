package com.intimetec.newsaggregation.mapper;

import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.ReportedNews;
import com.intimetec.newsaggregation.entity.SavedArticle;

import java.util.List;

public class NewsMapper {

    public static List<NewsResponse> mapToNewsResponse(List<News> newsList) {
        return newsList
                .stream()
                .map(news -> NewsResponse.builder()
                        .headline(news.getHeadline())
                        .id(news.getId())
                        .url(news.getUrl())
                        .description(news.getDescription())
                        .categories(news.getCategories().stream().map(NewsCategory::getCategoryName).toList())
                        .source(news.getSource())
                        .publishedAt(news.getPublishedAt())
                        .isHidden(news.getIsHidden())
                        .build()
                )
                .toList();
    }

    public static List<SavedNewsResponse> mapToSavedNewsResponse(List<SavedArticle> newsList) {
        return newsList
                .stream()
                .map(savedNews -> SavedNewsResponse.builder()
                        .headline(savedNews.getNewsArticle().getHeadline())
                        .id(savedNews.getNewsArticle().getId())
                        .url(savedNews.getNewsArticle().getUrl())
                        .description(savedNews.getNewsArticle().getDescription())
                        .categories(savedNews.getNewsArticle().getCategories().stream().map(NewsCategory::getCategoryName).toList())
                        .source(savedNews.getNewsArticle().getSource())
                        .build()
                )
                .toList();
    }

    public static List<ReportedNewsResponse> mapToReportedNewsResponse(List<ReportedNews> newsList) {
        return newsList
                .stream()
                .map(reportedNews -> ReportedNewsResponse.builder()
                        .headline(reportedNews.getReportedNews().getHeadline())
                        .newsId(reportedNews.getReportedNews().getId())
                        .url(reportedNews.getReportedNews().getUrl())
                        .description(reportedNews.getReportedNews().getDescription())
                        .categories(reportedNews.getReportedNews().getCategories().stream().map(NewsCategory::getCategoryName).toList())
                        .source(reportedNews.getReportedNews().getSource())
                        .publishedAt(reportedNews.getReportedNews().getPublishedAt())
                        .isHidden(reportedNews.getReportedNews().getIsHidden())
                        .reportedByUserEmail(reportedNews.getReportedBy().getEmail())
                        .build()
                )
                .toList();
    }

}
