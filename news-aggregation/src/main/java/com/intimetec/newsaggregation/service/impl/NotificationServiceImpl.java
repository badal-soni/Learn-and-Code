package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.entity.Notification;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NotificationRepository;
import com.intimetec.newsaggregation.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public List<NotificationResponse> getAllUnreadNotificationsOfUser(User user) {
        List<Long> notificationIds = new ArrayList<>();
        final List<Notification> unReadNotifications = notificationRepository.findAllUnReadNotificationsByReceiver(user);
        unReadNotifications.forEach(notification -> notificationIds.add(notification.getId()));
        notificationRepository.updateReadStatus(notificationIds, true);
        return unReadNotifications
                .stream()
                .map(notification -> {
                    NotificationResponse notificationResponse = new NotificationResponse();
                    notificationResponse.setNotificationId(notification.getId());
                    notificationResponse.setContent(notification.getContent());
                    return notificationResponse;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long notificationId, User user) {
        final Notification notification = notificationRepository
                .findByIdAndReceiver(notificationId, user)
                .orElseThrow(() -> new BadRequestException("Notification with id: " + notificationId + " does not exists"));
        notificationRepository.updateReadStatus(List.of(notificationId), true);
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setNotificationId(notificationId);
        notificationResponse.setContent(notification.getContent());
        return notificationResponse;
    }

}
