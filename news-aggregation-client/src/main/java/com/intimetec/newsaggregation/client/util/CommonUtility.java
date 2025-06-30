package com.intimetec.newsaggregation.client.util;

import com.intimetec.newsaggregation.client.dto.response.ApiResponse;

import java.util.Set;

public class CommonUtility {

    public static String getFirstRole(Set<String> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        return roles.iterator().next();
    }

    public static <T> T getDataOrElseThrow(
            ApiResponse<T> response,
            Exception exception
    ) throws Exception {
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw exception;
        }
    }

}
