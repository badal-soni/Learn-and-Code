package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface NotificationService {

    List<NotificationResponse> viewNotifications(User user);

}
