package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.response.NotificationResponse;
import com.intimetec.newsaggregation.entity.Notification;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.NotificationRepository;
import com.intimetec.newsaggregation.service.impl.NotificationServiceImpl;
import com.intimetec.newsaggregation.util.MockDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUnreadNotificationsOfUser_shouldReturnUnreadNotifications() {
        User user = MockDataCreator.createMockUser();
        List<Notification> notifications = List.of(MockDataCreator.createMockNotification());
        when(notificationRepository.findAllUnReadNotificationsByReceiver(user)).thenReturn(notifications);

        List<NotificationResponse> responses = notificationService.getAllUnreadNotificationsOfUser(user);

        assertEquals(1, responses.size());
        verify(notificationRepository, times(1)).updateReadStatus(anyList(), eq(true));
    }

    @Test
    void getNotificationById_shouldReturnNotification() {
        User user = MockDataCreator.createMockUser();
        Notification notification = MockDataCreator.createMockNotification();
        when(notificationRepository.findByIdAndReceiver(notification.getId(), user)).thenReturn(Optional.of(notification));

        NotificationResponse response = notificationService.getNotificationById(notification.getId(), user);

        assertNotNull(response);
        assertEquals(notification.getContent(), response.getContent());
        verify(notificationRepository, times(1)).updateReadStatus(anyList(), eq(true));
    }
}