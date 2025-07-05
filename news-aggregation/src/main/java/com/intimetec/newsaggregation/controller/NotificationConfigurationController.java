package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.dto.request.UpdateNotificationPreferencesRequest;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.NotificationConfigurationService;
import com.intimetec.newsaggregation.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
        path = ApiVersions.V1_NOTIFICATIONS_CONFIGURATION,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class NotificationConfigurationController {

    private final NotificationConfigurationService notificationConfigurationService;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse> updateNotificationPreferences(
            @RequestBody UpdateNotificationPreferencesRequest updateNotificationPreferencesRequest,
            @CurrentUser User currentUser
    ) {
        notificationConfigurationService.updateNotificationPreferences(updateNotificationPreferencesRequest, currentUser);
        return HttpUtil.sendResponseWithNoData(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse> viewNotificationPreferences(@CurrentUser User currentUser) {
        return HttpUtil.sendResponseWithData(
                notificationConfigurationService.getAllNotificationConfigurations(currentUser),
                HttpStatus.OK
        );
    }

}
