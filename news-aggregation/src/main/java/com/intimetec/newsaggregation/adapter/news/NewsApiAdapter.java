package com.intimetec.newsaggregation.adapter.news;

import com.intimetec.newsaggregation.classifier.NewsCategoryClassifier;
import com.intimetec.newsaggregation.configuration.NewsApiResponseAttributes;
import com.intimetec.newsaggregation.constant.PredefinedCategories;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.repository.*;
import com.intimetec.newsaggregation.util.ApiClient;
import com.intimetec.newsaggregation.util.CommonUtility;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class NewsApiAdapter extends BaseNewsAdapter implements NewsFetcher {

    private final NewsRepository newsRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsCategoryClassifier newsCategoryClassifier;
    private final ConcurrentHashMap<String, NewsCategory> categoryCache = new ConcurrentHashMap<>();

    private static final String NEWS_API = "News API";

    public NewsApiAdapter(
            final NewsRepository newsRepository,
            final NewsCategoryRepository newsCategoryRepository,
            final ExternalServerDetailsRepository externalServerDetailsRepository,
            final ApiClient apiClient,
            final NewsCategoryClassifier newsCategoryClassifier,
            final BlockedKeywordRepository blockedKeywordRepository
    ) {
        super(externalServerDetailsRepository, newsCategoryRepository, blockedKeywordRepository, apiClient);
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.newsCategoryClassifier = newsCategoryClassifier;
    }

    @Override
    @Transactional
    public List<News> fetchNews() {
        final String response = super.getNewsData(NEWS_API);
        if (Objects.isNull(response)) {
            return List.of();
        }
        try {
            return newsRepository.saveAll(parseNewsFromJsonResponse(response));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return List.of();
        }
    }

    @Override
    public String getServiceName() {
        return NEWS_API;
    }

    @Override
    protected List<News> parseNewsFromJsonResponse(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        JSONArray newsArticleJsonResponse = json.getJSONArray(NewsApiResponseAttributes.ARTICLES);

        List<News> newsArticles = new ArrayList<>();
        for (int i = 0; i < newsArticleJsonResponse.length(); i++) {
            final JSONObject news = newsArticleJsonResponse.getJSONObject(i);
            final String assignedCategory = assignCategory(news);
            final NewsCategory newsCategory = getFromCache(assignedCategory);

            News newsEntity = News.builder()
                    .headline(news.optString(NewsApiResponseAttributes.TITLE, NewsApiResponseAttributes.NOT_ANY))
                    .description(news.optString(NewsApiResponseAttributes.CONTENT, NewsApiResponseAttributes.NOT_ANY))
                    .url(news.optString(NewsApiResponseAttributes.URL, NewsApiResponseAttributes.NOT_ANY))
                    .source(news.optJSONObject(NewsApiResponseAttributes.SOURCE).optString(NewsApiResponseAttributes.SOURCE_NAME, NewsApiResponseAttributes.NOT_ANY))
                    .publishedAt(CommonUtility.convertToLocalDate(news.optString(NewsApiResponseAttributes.PUBLISHED_AT, NewsApiResponseAttributes.EMPTY_DATE)))
                    .categories(List.of(newsCategory))
                    .build();
            final boolean shouldHide = super.shouldNewsBeHidden(
                    newsEntity.getHeadline(),
                    newsEntity.getDescription(),
                    List.of(assignedCategory)
            );
            newsEntity.setIsHidden(shouldHide);
            newsArticles.add(newsEntity);
        }
        return newsArticles;
    }

    private String assignCategory(JSONObject jsonObject) {
        final String headline = jsonObject.optString(NewsApiResponseAttributes.TITLE, NewsApiResponseAttributes.NOT_ANY);
        final String description = jsonObject.optString(NewsApiResponseAttributes.CONTENT, NewsApiResponseAttributes.NOT_ANY);
        if (Objects.isNull(headline) && Objects.isNull(description)) {
            return PredefinedCategories.GENERAL;
        }
        return this.newsCategoryClassifier.classifyNewsCategory(headline, description);
    }

    private NewsCategory getFromCache(String assignedCategory) {
        final NewsCategory newsCategory;
        if (categoryCache.containsKey(assignedCategory)) {
            return categoryCache.get(assignedCategory);
        }
        newsCategory = createCategoryIfNotExists(assignedCategory);
        categoryCache.put(assignedCategory, newsCategory);
        return newsCategory;
    }

    private NewsCategory createCategoryIfNotExists(String categoryName) {
        Optional<NewsCategory> existingCategory = newsCategoryRepository.findByCategoryName(categoryName);
        if (existingCategory.isPresent()) {
            return existingCategory.get();
        } else {
            NewsCategory newCategory = new NewsCategory();
            newCategory.setCategoryName(categoryName);
            newCategory.setHidden(false);
            return newsCategoryRepository.saveAndFlush(newCategory);
        }
    }

}