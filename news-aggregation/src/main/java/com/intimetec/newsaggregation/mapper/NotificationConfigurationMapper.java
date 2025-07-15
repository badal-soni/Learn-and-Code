package com.intimetec.newsaggregation.mapper;

import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.dto.response.NotificationConfigurationResponse;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.NotificationConfiguration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotificationConfigurationMapper {

    public static AllNotificationConfigurations mapToDTO(
            long userId,
            List<NewsCategory> allNewsCategories,
            List<NotificationConfiguration> existingConfigurations
    ) {
        Map<String, NotificationConfiguration> existingConfigurationsMap = existingConfigurations
                .stream()
                .collect(Collectors.toMap(
                        config -> config.getNewsCategory().getCategoryName(),
                        config -> config,
                        (existing, replacement) -> {
                            return existing.getUpdatedAt().isAfter(replacement.getUpdatedAt())
                                    ? existing
                                    : replacement;
                        }
                ));

        List<NotificationConfigurationResponse> notificationConfigurationResponse = allNewsCategories.stream()
                .map(category -> {
                    NotificationConfiguration existingConfig = existingConfigurationsMap.get(category.getCategoryName());
                    boolean categoryEnabled = existingConfig != null && existingConfig.isEnabled();
                    List<KeywordResponse> keywordResponses = category.getKeywords().stream()
                            .map(keyword -> {
                                boolean isKeywordEnabled = existingConfig != null &&
                                        existingConfig.isEnabled() &&
                                        keyword.getParentCategory().getCategoryName().equals(existingConfig.getNewsCategory().getCategoryName());
                                KeywordResponse keywordResponse = new KeywordResponse();
                                keywordResponse.setKeyword(keyword.getKeyword());
                                keywordResponse.setParentCategory(keyword.getParentCategory().getCategoryName());
                                keywordResponse.setEnabled(isKeywordEnabled);
                                keywordResponse.setKeywordId(keyword.getId());
                                return keywordResponse;
                            })
                            .collect(Collectors.toList());

                    NotificationConfigurationResponse categoryResponse = new NotificationConfigurationResponse();
                    categoryResponse.setCategoryName(category.getCategoryName());
                    categoryResponse.setEnabled(categoryEnabled);
                    categoryResponse.setKeywords(keywordResponses);
                    return categoryResponse;
                })
                .collect(Collectors.toList());

        AllNotificationConfigurations allConfigs = new AllNotificationConfigurations();
        allConfigs.setUserId(userId);
        allConfigs.setNewsCategories(notificationConfigurationResponse);
        return allConfigs;
    }

}
