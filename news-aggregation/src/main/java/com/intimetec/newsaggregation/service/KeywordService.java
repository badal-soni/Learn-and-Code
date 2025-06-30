package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.entity.User;

import java.util.List;

public interface KeywordService {

    void createKeyword(CreateKeywordRequest createKeywordRequest, User user);

    void toggleKeywordActiveStatus(Long keywordId, User user);

    List<KeywordResponse> getAllKeywordsOfUser(User user);

}
