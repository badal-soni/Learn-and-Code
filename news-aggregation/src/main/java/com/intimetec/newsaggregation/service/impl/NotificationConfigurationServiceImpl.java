package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.event.UserRegisteredEvent;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.NotificationConfiguration;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.mapper.NotificationConfigurationMapper;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NotificationConfigurationRepository;
import com.intimetec.newsaggregation.repository.UserRepository;
import com.intimetec.newsaggregation.service.NotificationConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationConfigurationServiceImpl implements NotificationConfigurationService {

    private final NotificationConfigurationRepository notificationConfigurationRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void updateNotificationPreferences(
            UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest,
            User user
    ) {
        List<NewsCategory> allNewsCategories = newsCategoryRepository.findAll();
        List<NotificationConfiguration> existingNotificationConfigurations = notificationConfigurationRepository.findAllByUser(user);

        Map<String, NotificationConfiguration> existingConfigurationsMap = new HashMap<>();
        for (NotificationConfiguration config : existingNotificationConfigurations) {
            existingConfigurationsMap.put(config.getNewsCategory().getCategoryName(), config);
        }

        Map<String, Boolean> categoryNameToEnabledStatusMap = new HashMap<>();
        for (UpdateNotificationPreferencesRequest.NotificationPreference preference : updateNotificationPreferencesRequest.getPreferences()) {
            categoryNameToEnabledStatusMap.put(preference.newsCategoryName(), preference.shouldEnableNotification());
        }

        List<NotificationConfiguration> updatedNotificationConfigurations = new ArrayList<>();
        for (NewsCategory newsCategory : allNewsCategories) {
            String categoryName = newsCategory.getCategoryName();
            NotificationConfiguration configuration = existingConfigurationsMap.get(categoryName);

            if (configuration != null && categoryNameToEnabledStatusMap.containsKey(categoryName)) {
                configuration.setEnabled(categoryNameToEnabledStatusMap.get(categoryName));
            } else {
                boolean isEnabled = categoryNameToEnabledStatusMap.getOrDefault(categoryName, false);
                configuration = new NotificationConfiguration();
                configuration.setNewsCategory(newsCategory);
                configuration.setUser(user);
                configuration.setEnabled(isEnabled);
            }
            updatedNotificationConfigurations.add(configuration);
        }
        notificationConfigurationRepository.saveAll(updatedNotificationConfigurations);
    }

    @EventListener
    public void populateDefaultNotificationPreferences(UserRegisteredEvent userRegisteredEvent) {
        final List<NewsCategory> newsCategories = newsCategoryRepository.findAll();
        final Optional<User> user = userRepository.findById(userRegisteredEvent.getUserId());
        newsCategories.forEach(newsCategory -> {
            NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
            notificationConfiguration.setUser(user.get());
            notificationConfiguration.setNewsCategory(newsCategory);
            notificationConfiguration.setEnabled(false);
            notificationConfigurationRepository.saveAndFlush(notificationConfiguration);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public AllNotificationConfigurations getAllNotificationConfigurations(User user) {
        List<NewsCategory> allNewsCategories = newsCategoryRepository.findAllByIsHiddenFalse();
        List<NotificationConfiguration> existingConfigurations = notificationConfigurationRepository.findAllByUserAndNewsCategoryIsHiddenFalse(user);
        return NotificationConfigurationMapper.mapToDTO(user.getId(), allNewsCategories, existingConfigurations);
    }

}
