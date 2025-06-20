package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.entity.User;

public interface NotificationConfigurationService {

    // todo: by default all preferences should be disabled (need to confirm this)
    void updateNotificationPreferences(
            UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest,
            User user
    );

    void populateDefaultNotificationPreferences(User user);

}
