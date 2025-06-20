package com.intimetec.newsaggregation.service;

import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.entity.User;

public interface KeywordService {

    void createKeyword(CreateKeywordRequest createKeywordRequest, User user);

    void activateKeyword(Long keywordId, User user);

    void deactivateKeyword(Long keywordId, User user);

}
