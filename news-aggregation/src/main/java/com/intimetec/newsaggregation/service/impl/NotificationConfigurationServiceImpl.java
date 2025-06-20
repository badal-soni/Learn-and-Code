package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.NotificationConfiguration;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NotificationConfigurationRepository;
import com.intimetec.newsaggregation.service.NotificationConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationConfigurationServiceImpl implements NotificationConfigurationService {

    private final NotificationConfigurationRepository notificationConfigurationRepository;
    private final NewsCategoryRepository newsCategoryRepository;

    @Override
    @Transactional
    public void updateNotificationPreferences(
            UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest,
            User user
    ) {
        // todo: after user registration, add default notification preferences for the user (all are disabled)
        Map<String, Boolean> categoryNameToEnableStatus = new HashMap<>();
        for (UpdateNotificationPreferencesRequest.NotificationPreference preference : updateNotificationPreferencesRequest.getPreferences()) {
            categoryNameToEnableStatus.putIfAbsent(preference.newsCategoryName(), preference.shouldEnableNotification());
        }
        List<NotificationConfiguration> notificationConfigurations = notificationConfigurationRepository.findByUserAndNewsCategory(
                user.getId(),
                categoryNameToEnableStatus.keySet()
        );
        List<NotificationConfiguration> configuredNotificationConfigurations = new ArrayList<>();
        notificationConfigurations.forEach(config -> {
            config.setEnabled(categoryNameToEnableStatus.get(config.getNewsCategory().getCategoryName()));
            configuredNotificationConfigurations.add(config);
        });

        notificationConfigurationRepository.saveAll(configuredNotificationConfigurations);
    }

    @Override
    @Transactional
    public void populateDefaultNotificationPreferences(User user) {
        final List<NewsCategory> newsCategories = newsCategoryRepository.findAll();
        for (NewsCategory newsCategory : newsCategories) {
            NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
            notificationConfiguration.setUser(user);
            notificationConfiguration.setNewsCategory(newsCategory);
            notificationConfiguration.setEnabled(false); // Default to disabled
            notificationConfigurationRepository.saveAndFlush(notificationConfiguration);
        }
    }

}
