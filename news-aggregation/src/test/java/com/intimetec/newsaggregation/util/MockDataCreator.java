package com.intimetec.newsaggregation.util;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.constant.NotificationType;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.event.UserRegisteredEvent;
import com.intimetec.newsaggregation.dto.request.*;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import com.intimetec.newsaggregation.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockDataCreator {

    public static SignUpRequest createMockSignUpRequestData() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("john@example.com");
        request.setPassword("pass123");
        request.setUsername("john");
        return request;
    }

    public static SignInRequest createMockSignInRequestData() {
        SignInRequest request = new SignInRequest();
        request.setEmail("john@example.com");
        request.setPassword("pass123");
        return request;
    }

    public static SavedNewsResponse createMockSavedNewsResponse() {
        return SavedNewsResponse.builder()
                .id(1L)
                .headline("Mock Headline")
                .description("Mock Description")
                .source("Mock Source")
                .url("http://mockurl.com")
                .categories(List.of("Category1", "Category2"))
                .build();
    }


    public static News createMockNews() {
        return News.builder()
                .headline("Mock Headline")
                .description("Mock Description")
                .source("Mock Source")
                .url("http://mockurl.com")
                .publishedAt(LocalDate.now())
                .isHidden(false)
                .categories(List.of(
                        createMockNewsCategory("Category1"),
                        createMockNewsCategory("Category2")
                ))
                .build();
    }

    public static NewsCategory createMockNewsCategory(String categoryName) {
        NewsCategory category = new NewsCategory();
        category.setId(1L);
        category.setCategoryName(categoryName);
        category.setHidden(false);
        return category;
    }

    public static SavedArticle createMockSavedArticle() {
        SavedArticle savedArticle = new SavedArticle();
        savedArticle.setSavedBy(createMockUser());
        savedArticle.setNewsArticle(createMockNews());
        return savedArticle;
    }

    public static User createMockUser() {
        User user = User.builder()
                .email("mockuser@example.com")
                .username("MockUser")
                .build();
        user.setId(1L);
        return user;
    }

    public static RegisterExternalServerRequest createMockRegisterExternalServerRequest() {
        RegisterExternalServerRequest request = new RegisterExternalServerRequest();
        request.setApiKey("apiKey");
        request.setServerName("Mock Server");
        request.setApiUrl("http://mockserver.url");
        return request;
    }

    public static UpdateExternalServerDetailsRequest createMockUpdateExternalServerDetailsRequest() {
        UpdateExternalServerDetailsRequest request = new UpdateExternalServerDetailsRequest();
        request.setServerId(1L);
        request.setEnabled(true);
        request.setApiKeyToUpdate("apiKey");
        return request;
    }

    public static ExternalServerDetail createMockExternalServerDetail() {
        ExternalServerDetail externalServerDetail = new ExternalServerDetail();
        externalServerDetail.setId(1L);
        externalServerDetail.setServerName("serverName");
        externalServerDetail.setApiKey("mockApiKey");
        externalServerDetail.setLastAccessedDate(LocalDate.now());
        externalServerDetail.setActive(true);
        externalServerDetail.setLastFailedTime(LocalDateTime.now());
        return externalServerDetail;
    }

    public static CreateKeywordRequest createMockCreateKeywordRequest() {
        CreateKeywordRequest request = new CreateKeywordRequest();
        request.setKeyword("mockKeyword");
        request.setParentCategory("mockCategory");
        return request;
    }

    public static Keyword createMockKeyword() {
        Keyword keyword = new Keyword();
        keyword.setId(1L);
        keyword.setKeyword("mockKeyword");
        keyword.setActive(true);
        keyword.setUsers(new ArrayList<>());
        keyword.setParentCategory(createMockNewsCategory());
        return keyword;
    }

    public static NewsCategory createMockNewsCategory() {
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setId(1L);
        newsCategory.setCategoryName("mockCategory");
        return newsCategory;
    }

    public static BlockedKeyword createMockBlockedKeyword() {
        BlockedKeyword blockedKeyword = new BlockedKeyword();
        blockedKeyword.setBlockedKeyword("mockBlockedKeyword");
        return blockedKeyword;
    }

    public static CreateCategoryRequest createMockCreateCategoryRequest() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setCategoryName("mockCategory");
        return request;
    }

    public static NotificationConfiguration createMockNotificationConfiguration() {
        NotificationConfiguration config = new NotificationConfiguration();
        config.setEnabled(true);
        return config;
    }

    public static NewsResponse createMockNewsResponse() {
        NewsResponse response = new NewsResponse();
        response.setHeadline("mockHeadline");
        response.setDescription("mockDescription");
        return response;
    }

    public static NewsReadHistory createMockNewsReadHistory() {
        NewsReadHistory newsReadHistory = new NewsReadHistory();
        newsReadHistory.setNews(createMockNews());
        newsReadHistory.setReadBy(createMockUser());
        return newsReadHistory;
    }

    public static NewsLikes createMockNewsLikes() {
        NewsLikes newsLikes = new NewsLikes();
        newsLikes.setNews(createMockNews());
        newsLikes.setInteractionUser(createMockUser());
        newsLikes.setInteractionType(NewsInteractionType.LIKE);
        return newsLikes;
    }

    public static Notification createMockNotification() {
        Notification notification = new Notification();
        notification.setContent("Mock Notification Content");
        notification.setHasRead(false);
        notification.setNotificationType(NotificationType.EMAIL);
        notification.setNewsId(1L);
        notification.setReceiver(createMockUser());
        return notification;
    }

    public static UpdateNotificationPreferencesRequest createMockUpdateNotificationPreferencesRequest() {
        UpdateNotificationPreferencesRequest request = new UpdateNotificationPreferencesRequest();
        request.setPreferences(List.of(
                new UpdateNotificationPreferencesRequest.NotificationPreference("Category1", true)
        ));
        return request;
    }

    public static UserRegisteredEvent createMockUserRegisteredEvent() {
        UserRegisteredEvent event = new UserRegisteredEvent();
        event.setUserId(1L);
        return event;
    }

    public static ReportNewsArticleRequest createMockReportNewsArticleRequest() {
        ReportNewsArticleRequest request = new ReportNewsArticleRequest();
        request.setReportReason("Mock Reason");
        return request;
    }

    public static ReportedNews createMockReportedNews() {
        ReportedNews reportedNews = new ReportedNews();
        reportedNews.setReportedBy(createMockUser());
        reportedNews.setReportedNews(createMockNews());
        reportedNews.setReportReason("Mock Reason");
        return reportedNews;
    }

    public static NewsIds createMockNewsIds() {
        NewsIds newsIds = new NewsIds();
        newsIds.setNewsIds(List.of(1L, 2L));
        return newsIds;
    }

    public static Keywords createMockKeywords() {
        Keywords keywords = new Keywords();
        keywords.setKeywords(List.of("keyword1", "keyword2"));
        return keywords;
    }

}
