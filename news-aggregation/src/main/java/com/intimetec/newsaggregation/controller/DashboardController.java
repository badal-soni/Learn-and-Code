package com.intimetec.newsaggregation.controller;

import com.intimetec.newsaggregation.annotation.CurrentUser;
import com.intimetec.newsaggregation.constant.ApiVersions;
import com.intimetec.newsaggregation.dto.response.ApiSuccessResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.service.DashboardService;
import com.intimetec.newsaggregation.util.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        path = ApiVersions.V1_DASHBOARD,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping(path = "/news/saved")
    public ResponseEntity<ApiSuccessResponse> getSavedNewsOfUser(@CurrentUser User currentUser) {
        return HttpUtil.sendResponseWithData(
                dashboardService.getAllNewsSavedByUser(currentUser),
                HttpStatus.OK
        );
    }

}
