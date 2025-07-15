package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface DashboardService {

    List<SavedNewsResponse> getAllNewsSavedByUser(User user);

}
