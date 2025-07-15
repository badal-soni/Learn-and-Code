package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.NotificationService;
import com.intimetec.newsaggregation.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = ApiVersions.V1_NOTIFICATIONS,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiSuccessResponse> getAllUnreadNotificationsOfUser(@CurrentUser User user) {
        return HttpUtil.sendResponseWithData(
                notificationService.getAllUnreadNotificationsOfUser(user),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/{notificationId}")
    public ResponseEntity<ApiSuccessResponse> getNotificationById(
            @PathVariable("notificationId") Long notificationId,
            @CurrentUser User user
    ) {
        return HttpUtil.sendResponseWithData(
                notificationService.getNotificationById(notificationId, user),
                HttpStatus.OK
        );
    }

}
