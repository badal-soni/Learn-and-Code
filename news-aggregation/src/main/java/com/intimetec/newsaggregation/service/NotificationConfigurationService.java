package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.entity.User;

public interface NotificationConfigurationService {

    void updateNotificationPreferences(
            UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest,
            User user
    );


    AllNotificationConfigurations getAllNotificationConfigurations(User user);

}
