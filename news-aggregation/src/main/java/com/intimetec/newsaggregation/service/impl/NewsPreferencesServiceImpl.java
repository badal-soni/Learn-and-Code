package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.Keyword;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NotificationConfiguration;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.mapper.NewsMapper;
import com.intimetec.newsaggregation.repository.NewsLikesRepository;
import com.intimetec.newsaggregation.repository.NewsReadHistoryRepository;
import com.intimetec.newsaggregation.repository.SavedArticleRepository;
import com.intimetec.newsaggregation.service.NewsPreferencesService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsPreferencesServiceImpl implements NewsPreferencesService {

    private static final double CATEGORY_WEIGHT = 0.4;
    private static final double NOTIFICATION_WEIGHT = 0.3;
    private static final double READ_SCORE = 0.1;
    private static final double LIKE_SCORE = 0.1;
    private static final double SAVE_SCORE = 0.1;

    private static final double CATEGORY_NEWS_WEIGHT = 0.7;
    private static final double CATEGORY_READ_SCORE = 0.05;
    private static final double CATEGORY_LIKE_SCORE = 0.1;
    private static final double CATEGORY_SAVE_SCORE = 0.05;
    private static final double KEYWORD_WEIGHT = 0.1;

    private static final double NOTIFICATION_BASE_SCORE = 0.5;
    private static final double KEYWORD_MATCH_SCORE = 0.25;
    private static final double MAX_SCORE = 1.0;

    private static final int LIKE_INTERACTION_WEIGHT = 2;
    private static final int READ_INTERACTION_WEIGHT = 1;
    private static final int SAVE_INTERACTION_WEIGHT = 2;
    private static final int MIN_INTERACTIONS = 1;

    private final NewsReadHistoryRepository newsReadHistoryRepository;
    private final NewsLikesRepository newsLikesRepository;
    private final SavedArticleRepository savedArticleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> sortNewsByUserPreferences(
            @NotNull User user,
            List<News> newsList
    ) {
        if (Objects.isNull(newsList) || newsList.isEmpty()) {
            return Collections.emptyList();
        }
        Map<Long, Double> newsScores = new HashMap<>();
        newsList.forEach(news -> newsScores.put(news.getId(), 0.0));

        Set<Long> readNewsIds = new HashSet<>(newsReadHistoryRepository.findAllIdsByReadBy(user));
        Set<Long> likedNewsIds = new HashSet<>(newsLikesRepository.findAllLikedNewsIds(user.getId()));
        Set<Long> savedNewsIds = new HashSet<>(savedArticleRepository.findAllNewsIdsBySavedById(user.getId()));

        Map<Long, Double> categoryInteractionScores = calculateCategoryInteractionScores(
                user.getId(),
                newsList
        );

        List<NotificationConfiguration> enabledConfigs = user.getNotificationConfigurations()
                .stream()
                .filter(NotificationConfiguration::isEnabled)
                .toList();

        newsList.forEach(news -> {
            double score = 0.0;

            score += news
                    .getCategories()
                    .stream()
                    .mapToDouble(category -> categoryInteractionScores.getOrDefault(category.getId(), 0.0))
                    .sum() * CATEGORY_WEIGHT;

            if (readNewsIds.contains(news.getId())) {
                score += READ_SCORE;
            }
            if (likedNewsIds.contains(news.getId())) {
                score += LIKE_SCORE;
            }
            if (savedNewsIds.contains(news.getId())) {
                score += SAVE_SCORE;
            }

            score += calculateNotificationScore(news, enabledConfigs) * NOTIFICATION_WEIGHT;
            newsScores.put(news.getId(), score);
        });

        List<News> sortedNews = newsList.stream()
                .sorted((n1, n2) -> Double.compare(newsScores.get(n2.getId()), newsScores.get(n1.getId())))
                .toList();
        return NewsMapper.mapToNewsResponse(sortedNews);
    }

    private Map<Long, Double> calculateCategoryInteractionScores(Long userId, List<News> allNews) {
        Map<Long, Integer> categoryInteractions = new HashMap<>();
        Map<Long, Double> categoryScores = new HashMap<>();

        allNews.forEach(news -> {
            boolean isRead = news
                    .getReadHistory()
                    .stream()
                    .anyMatch(history -> history.getReadBy().getId() == (userId));
            boolean isLiked = news
                    .getUserInteractions()
                    .stream()
                    .anyMatch(interaction -> interaction.getInteractionUser().getId() == (userId));
            boolean isSaved = news
                    .getSavedNews()
                    .stream()
                    .anyMatch(saved -> saved.getSavedBy().getId() == (userId));

            news.getCategories().forEach(category -> {
                int score = categoryInteractions.getOrDefault(category.getId(), 0);
                if (isRead) {
                    score += READ_INTERACTION_WEIGHT;
                }
                if (isLiked) {
                    score += LIKE_INTERACTION_WEIGHT;
                }
                if (isSaved) {
                    score += SAVE_INTERACTION_WEIGHT;
                }
                categoryInteractions.put(category.getId(), score);
            });
        });
        int maxInteractions = categoryInteractions
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(MIN_INTERACTIONS);

        categoryInteractions.forEach((categoryId, interactions) ->
                categoryScores.put(categoryId, (double) interactions / maxInteractions)
        );

        return categoryScores;
    }

    private double calculateNotificationScore(News news, List<NotificationConfiguration> configs) {
        double score = 0.0;

        for (NotificationConfiguration config : configs) {
            if (news.getCategories().contains(config.getNewsCategory())) {
                score += NOTIFICATION_BASE_SCORE;

                if (config.getNewsCategory().getKeywords() != null && !config.getNewsCategory().getKeywords().isEmpty()) {
                    for (String keyword : config.getNewsCategory().getKeywords().stream().map(Keyword::getKeyword).toList()) {
                        if (news.getHeadline().toLowerCase().contains(keyword.toLowerCase()) ||
                                news.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                            score += KEYWORD_MATCH_SCORE;
                        }
                    }
                }
            }
        }

        return Math.min(score, MAX_SCORE);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> sortCategoryNews(
            @NotNull User user,
            List<News> categoryNewsList
    ) {
        if (Objects.isNull(categoryNewsList) || categoryNewsList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> readNewsIds = new HashSet<>(newsReadHistoryRepository.findAllIdsByReadBy(user));
        Set<Long> likedNewsIds = new HashSet<>(newsLikesRepository.findAllLikedNewsIds(user.getId()));
        Set<Long> savedNewsIds = new HashSet<>(savedArticleRepository.findAllNewsIdsBySavedById(user.getId()));

        Map<Long, Double> categoryInteractionScores = calculateCategoryInteractionScores(user.getId(), categoryNewsList);
        Map<Long, Double> newsScores = new HashMap<>();

        categoryNewsList.forEach(news -> {
            double score = 0.0;

            double categoryScore = news.getCategories().stream()
                    .mapToDouble(category -> categoryInteractionScores.getOrDefault(category.getId(), 0.0))
                    .average()
                    .orElse(0.0);
            score += categoryScore * CATEGORY_NEWS_WEIGHT;

            if (readNewsIds.contains(news.getId())) {
                score += CATEGORY_READ_SCORE;
            }
            if (likedNewsIds.contains(news.getId())) {
                score += CATEGORY_LIKE_SCORE;
            }
            if (savedNewsIds.contains(news.getId())) {
                score += CATEGORY_SAVE_SCORE;
            }

            double keywordScore = calculateKeywordMatchScore(news);
            score += keywordScore * KEYWORD_WEIGHT;

            newsScores.put(news.getId(), score);
        });
        List<News> sortedNews = categoryNewsList.stream()
                .sorted((n1, n2) -> Double.compare(newsScores.get(n2.getId()), newsScores.get(n1.getId())))
                .toList();

        return NewsMapper.mapToNewsResponse(sortedNews);
    }

    private double calculateKeywordMatchScore(News news) {
        double score = 0.0;

        Set<Keyword> categoryKeywords = news.getCategories().stream()
                .flatMap(category -> category.getKeywords().stream())
                .filter(Keyword::isActive)
                .collect(Collectors.toSet());

        if (categoryKeywords.isEmpty()) {
            return 0.0;
        }

        String newsText = (news.getHeadline() + " " + news.getDescription()).toLowerCase();
        long matchCount = categoryKeywords.stream()
                .filter(keyword -> newsText.contains(keyword.getKeyword().toLowerCase()))
                .count();
        if (matchCount > 0) {
            score = Math.min(MAX_SCORE, matchCount / (double) categoryKeywords.size());
        }

        return score;
    }

}