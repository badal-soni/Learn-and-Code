package com.intimetec.newsaggregation.adapter;

import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.util.CommonUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TheNewsApiAdapter implements NewsFetcher {

    private final NewsRepository newsRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final ExternalServerDetailsRepository externalServerDetailsRepository;
    private final RestTemplate restTemplate;

    private static final String THE_NEWS_API = "The News API";

    @Override
    public List<News> fetchNews() {
        Optional<ExternalServerDetail> externalServerDetailOptional = externalServerDetailsRepository.findByServerName(THE_NEWS_API);
        if (externalServerDetailOptional.isEmpty()) {
            log.error("Didn't find external server details for News API");
        }

        ExternalServerDetail newsApiDetail = externalServerDetailOptional.get();
        String url = newsApiDetail.getRequestUri() + newsApiDetail.getApiKey();

        String response;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, null, String.class).getBody();
        } catch (Exception exception) {
            return null;
        }

        JSONObject json = new JSONObject(response);
        JSONArray articles = json.getJSONArray("articles");

        List<News> newsArticles = new ArrayList<>();
        for (int i = 0; i < articles.length(); i++) {
            JSONObject news = articles.getJSONObject(i);

            // Create and save News entity
            News newsEntity = News.builder()
                    .headline(news.optString("title", "Untitled"))
                    .description(news.optString("content", ""))
                    .url(news.optString("url", ""))
                    .source(news.optJSONObject("source").optString("name", "Unknown"))
                    .publishedAt(CommonUtility.convertToLocalDate(news.optString("publishedAt", "")))
                    .build();
            newsEntity = newsRepository.saveAndFlush(newsEntity);

            // Assign category
            final String categoryToBeAssigned = assignCategory(newsEntity);

            NewsCategory newsCategory = newsCategoryRepository.findByCategoryName(categoryToBeAssigned)
                    .orElseGet(() -> {
                        NewsCategory newCategory = new NewsCategory();
                        newCategory.setCategoryName(categoryToBeAssigned);
                        return newsCategoryRepository.saveAndFlush(newCategory);
                    });

            if (newsEntity.getCategories() == null) {
                newsEntity.setCategories(new ArrayList<>());
            }

            // Add bidirectional relationships
            if (!newsEntity.getCategories().contains(newsCategory)) {
                newsEntity.getCategories().add(newsCategory);
            }
            if (!newsCategory.getNews().contains(newsEntity)) {
                newsCategory.getNews().add(newsEntity);
            }

            newsArticles.add(newsEntity);
        }
        return newsRepository.saveAll(newsArticles);
    }
}
