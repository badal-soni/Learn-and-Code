package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.response.SavedNewsResponse;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.mapper.NewsMapper;
import com.intimetec.newsaggregation.repository.SavedArticleRepository;
import com.intimetec.newsaggregation.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final SavedArticleRepository savedArticleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SavedNewsResponse> getAllNewsSavedByUser(User user) {
        return NewsMapper.mapToSavedNewsResponse(savedArticleRepository.findAllBySavedById(user.getId()));
    }

}
