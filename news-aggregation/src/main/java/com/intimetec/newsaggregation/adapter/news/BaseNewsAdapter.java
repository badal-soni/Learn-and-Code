package com.intimetec.newsaggregation.adapter.news;

import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.entity.BlockedKeyword;
import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.repository.BlockedKeywordRepository;
import com.intimetec.newsaggregation.repository.ExternalServerDetailsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.util.ApiClient;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseNewsAdapter {

    private final ExternalServerDetailsRepository externalServerDetailsRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final BlockedKeywordRepository blockedKeywordRepository;
    private final ApiClient apiClient;

    public BaseNewsAdapter(
            final ExternalServerDetailsRepository externalServerDetailsRepository,
            final NewsCategoryRepository newsCategoryRepository,
            final BlockedKeywordRepository blockedKeywordRepository,
            final ApiClient apiClient
    ) {
        this.externalServerDetailsRepository = externalServerDetailsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.blockedKeywordRepository = blockedKeywordRepository;
        this.apiClient = apiClient;
    }

    protected boolean shouldNewsBeHidden(
            @NotNull String headline,
            @NotNull String description,
            List<String> newsCategories
    ) {
        final String fullText = headline + description;
        final List<String> blockedKeywords = blockedKeywordRepository
                .findAll()
                .stream()
                .map(BlockedKeyword::getBlockedKeyword)
                .toList();
        for (String blockedKeyword : blockedKeywords) {
            if (fullText.contains(blockedKeyword)) {
                return true;
            }
        }
        boolean hasBlockedKeywords = doesNewsContainsBlockedKeywords(fullText);
        boolean areCategoriesHidden = isNewsCategoryHidden(newsCategories);
        return hasBlockedKeywords || areCategoriesHidden;
    }

    private boolean doesNewsContainsBlockedKeywords(String fullText) {
        fullText = fullText.toLowerCase();
        final List<String> blockedKeywords = blockedKeywordRepository
                .findAll()
                .stream()
                .map(BlockedKeyword::getBlockedKeyword)
                .toList();
        for (String blockedKeyword: blockedKeywords) {
            if (fullText.contains(blockedKeyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean isNewsCategoryHidden(List<String> newsCategories) {
        Set<String> hiddenNewsCategories = newsCategoryRepository
                .findAllByIsHiddenTrue()
                .stream()
                .map(newsCategory -> newsCategory.getCategoryName().toLowerCase())
                .collect(Collectors.toSet());

        for (String newsCategory: newsCategories) {
            if (hiddenNewsCategories.contains(newsCategory.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    protected void updateExternalServerDetail(
            ExternalServerDetail externalServerDetail,
            String response
    ) {
        if (Objects.isNull(response)) {
            log.warn(Messages.EMPTY_RESPONSE_FROM_EXTERNAL_SERVER, externalServerDetail.getServerName());
            externalServerDetail.setActive(false);
            externalServerDetail.setLastFailedTime(LocalDateTime.now());
        } else {
            externalServerDetail.setActive(true);
            externalServerDetail.setLastAccessedDate(LocalDate.now());
        }
        externalServerDetailsRepository.saveAndFlush(externalServerDetail);
    }

    protected String getNewsData(final String serviceName) {
        final Optional<ExternalServerDetail> externalServerDetailOptional = externalServerDetailsRepository.findByServerName(serviceName);
        if (externalServerDetailOptional.isEmpty()) {
            log.error(Messages.EXTERNAL_SERVER_NOT_FOUND, serviceName);
            return null;
        }

        final ExternalServerDetail externalServerDetail = externalServerDetailOptional.get();
        final String url = externalServerDetail.getRequestUri() + externalServerDetail.getApiKey();
        final String response = this.apiClient.get(url, String.class);
        updateExternalServerDetail(externalServerDetail, response);
        return response;
    }

    protected abstract List<News> parseNewsFromJsonResponse(String jsonResponse);

    protected Set<NewsCategory> getCategoriesToCreate(
            Set<String> categoriesFromResponse,
            Set<String> existingCategories
    ) {
        final Set<NewsCategory> categoriesToCreate = new HashSet<>();
        categoriesFromResponse.forEach(category -> {
            if (!existingCategories.contains(category)) {
                NewsCategory newsCategory = new NewsCategory();
                newsCategory.setCategoryName(category);
                categoriesToCreate.add(newsCategory);
            }
        });
        return categoriesToCreate;
    }

    protected Map<String, NewsCategory> buildCategoryNameToCategoryEntityMap(Set<String> categoriesSet) {
        final Set<NewsCategory> existingCategories = newsCategoryRepository.findAllByCategoryNameIn(categoriesSet);
        final Set<String> existingCategoryNames = existingCategories
                .stream()
                .map(NewsCategory::getCategoryName)
                .collect(Collectors.toSet());
        final Set<NewsCategory> categoriesToCreate = this.getCategoriesToCreate(categoriesSet, existingCategoryNames);
        if (categoriesToCreate.isEmpty()) {
            return buildMap(existingCategories);
        }
        final List<NewsCategory> createdCategories = newsCategoryRepository.saveAllAndFlush(categoriesToCreate);
        existingCategories.addAll(createdCategories);
        return buildMap(existingCategories);
    }

    private Map<String, NewsCategory> buildMap(Set<NewsCategory> newsCategories) {
        Map<String, NewsCategory> categoryMap = new HashMap<>();
        newsCategories.forEach(newsCategory -> {
            categoryMap.putIfAbsent(newsCategory.getCategoryName(), newsCategory);
        });
        return categoryMap;
    }

}
