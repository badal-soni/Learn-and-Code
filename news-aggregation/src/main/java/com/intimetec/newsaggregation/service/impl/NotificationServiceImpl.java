package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.entity.Notification;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.NotificationRepository;
import com.intimetec.newsaggregation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> viewNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllNotificationsByReceiver(user);
        notifications.forEach(notification -> {
            notification.setHasRead(true);
        });
        notifications = notificationRepository.saveAllAndFlush(notifications);
        List<NotificationResponse> notificationResponses = new ArrayList<>();
        notifications.forEach(notification -> {
            NotificationResponse notificationResponse = new NotificationResponse();
            notificationResponse.setContent(notification.getContent());
            notificationResponses.add(notificationResponse);
        });
        return notificationResponses;
    }
}
