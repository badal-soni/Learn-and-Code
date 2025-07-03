package com.intimetec.newsaggregation.adapter.news;

import com.intimetec.newsaggregation.configuration.NewsApiResponseAttributes;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.repository.BlockedKeywordRepository;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.util.ApiClient;
import com.intimetec.newsaggregation.util.CommonUtility;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Slf4j
public class TheNewsApiAdapter extends BaseNewsAdapter implements NewsFetcher {

    private final NewsRepository newsRepository;
    private static final String THE_NEWS_API = "The News API";

    public TheNewsApiAdapter(
            final NewsRepository newsRepository,
            final NewsCategoryRepository newsCategoryRepository,
            final ExternalServerDetailsRepository externalServerDetailsRepository,
            final BlockedKeywordRepository blockedKeywordRepository,
            final ApiClient apiClient
    ) {
        super(externalServerDetailsRepository, newsCategoryRepository, blockedKeywordRepository, apiClient);
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional
    public List<News> fetchNews() {
        final String response = super.getNewsData(THE_NEWS_API);
        if (Objects.isNull(response)) {
            return List.of();
        }
        try {
            return newsRepository.saveAll(parseNewsFromJsonResponse(response));
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return List.of();
        }
    }

    @Override
    public String getServiceName() {
        return THE_NEWS_API;
    }

    @Override
    protected List<News> parseNewsFromJsonResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONObject newsData = jsonResponse.getJSONObject(NewsApiResponseAttributes.DATA);
        JSONArray newsArray = newsData.getJSONArray(NewsApiResponseAttributes.GENERAL);

        List<News> newsArticles = new ArrayList<>();
        final Map<String, NewsCategory> categoryNameToCategoryEntityMap = findOrElseCreateCategories(newsArray);
        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject news = newsArray.getJSONObject(i);
            JSONArray categories = news.getJSONArray(NewsApiResponseAttributes.CATEGORIES);
            List<NewsCategory> newsCategories = new ArrayList<>();
            for (int j = 0; j < categories.length(); j++) {
                final String category = categories.getString(j);
                newsCategories.add(categoryNameToCategoryEntityMap.get(category));
            }
            News newsEntity = News.builder()
                    .headline(news.optString(NewsApiResponseAttributes.TITLE, NewsApiResponseAttributes.NOT_ANY))
                    .description(news.optString(NewsApiResponseAttributes.DESCRIPTION, NewsApiResponseAttributes.NOT_ANY))
                    .url(news.optString(NewsApiResponseAttributes.URL, NewsApiResponseAttributes.NOT_ANY))
                    .source(news.optString(NewsApiResponseAttributes.SOURCE, NewsApiResponseAttributes.NOT_ANY))
                    .publishedAt(CommonUtility.convertToLocalDate(news.optString(NewsApiResponseAttributes.PUBLISHED_AT, NewsApiResponseAttributes.EMPTY_DATE)))
                    .categories(newsCategories)
                    .build();
            boolean shouldHide = super.shouldNewsBeHidden(
                    newsEntity.getHeadline(),
                    newsEntity.getDescription(),
                    newsCategories.stream().map(NewsCategory::getCategoryName).toList()
            );
            newsEntity.setIsHidden(shouldHide);
            newsArticles.add(newsEntity);
        }
        return newsArticles;
    }

    private Map<String, NewsCategory> findOrElseCreateCategories(JSONArray jsonArray) {
        final Set<String> categoriesSet = new HashSet<>();
        for (int index = 0; index < jsonArray.length(); index++) {
            JSONArray categoriesArray = jsonArray.getJSONObject(index).getJSONArray(NewsApiResponseAttributes.CATEGORIES);
            for (int i = 0; i < categoriesArray.length(); i++) {
                categoriesSet.add(categoriesArray.getString(i));
            }
        }
        return super.buildCategoryNameToCategoryEntityMap(categoriesSet);
    }

}
