package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.event.UserRegisteredEvent;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.AllNotificationConfigurations;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.NotificationConfiguration;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NotificationConfigurationRepository;
import com.intimetec.newsaggregation.repository.UserRepository;
import com.intimetec.newsaggregation.service.impl.NotificationConfigurationServiceImpl;
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

class NotificationConfigurationServiceImplTest {

    @Mock
    private NotificationConfigurationRepository notificationConfigurationRepository;

    @Mock
    private NewsCategoryRepository newsCategoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationConfigurationServiceImpl notificationConfigurationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateNotificationPreferences_shouldUpdatePreferences() {
        User user = MockDataCreator.createMockUser();
        UpdateNotificationPreferencesRequest request = MockDataCreator.createMockUpdateNotificationPreferencesRequest();
        List<NewsCategory> newsCategories = List.of(MockDataCreator.createMockNewsCategory("Category1"));

        NotificationConfiguration notificationConfiguration = MockDataCreator.createMockNotificationConfiguration();
        notificationConfiguration.setNewsCategory(MockDataCreator.createMockNewsCategory());
        List<NotificationConfiguration> existingConfigurations = List.of(notificationConfiguration);

        when(newsCategoryRepository.findAll()).thenReturn(newsCategories);
        when(notificationConfigurationRepository.findAllByUser(user)).thenReturn(existingConfigurations);

        notificationConfigurationService.updateNotificationPreferences(request, user);

        verify(notificationConfigurationRepository, times(1)).saveAll(anyList());
    }

    @Test
    void populateDefaultNotificationPreferences_shouldPopulateDefaults() {
        UserRegisteredEvent event = MockDataCreator.createMockUserRegisteredEvent();
        User user = MockDataCreator.createMockUser();
        List<NewsCategory> newsCategories = List.of(MockDataCreator.createMockNewsCategory("Category1"));

        when(newsCategoryRepository.findAll()).thenReturn(newsCategories);
        when(userRepository.findById(event.getUserId())).thenReturn(Optional.of(user));

        notificationConfigurationService.populateDefaultNotificationPreferences(event);

        verify(notificationConfigurationRepository, times(newsCategories.size())).saveAndFlush(any());
    }

    @Test
    void getAllNotificationConfigurations_shouldReturnConfigurations() {
        User user = MockDataCreator.createMockUser();
        List<NewsCategory> newsCategories = List.of(MockDataCreator.createMockNewsCategory("Category1"));
        NotificationConfiguration notificationConfiguration = MockDataCreator.createMockNotificationConfiguration();
        notificationConfiguration.setNewsCategory(MockDataCreator.createMockNewsCategory());
        List<NotificationConfiguration> existingConfigurations = List.of(notificationConfiguration);

        when(newsCategoryRepository.findAllByIsHiddenFalse()).thenReturn(newsCategories);
        when(notificationConfigurationRepository.findAllByUserAndNewsCategoryIsHiddenFalse(user)).thenReturn(existingConfigurations);

        AllNotificationConfigurations configurations = notificationConfigurationService.getAllNotificationConfigurations(user);

        assertNotNull(configurations);
        assertEquals(1, configurations.getNewsCategories().size());
    }
}