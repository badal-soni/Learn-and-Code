package com.intimetec.newsaggregation.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AllNotificationConfigurations {

    private long userId;
    private List<NotificationConfigurationResponse> newsCategories;

}
