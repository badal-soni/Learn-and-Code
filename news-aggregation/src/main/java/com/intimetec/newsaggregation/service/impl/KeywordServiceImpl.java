package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.entity.Keyword;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.KeywordsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordServiceImpl implements KeywordService {

    private final KeywordsRepository keywordsRepository;
    private final NewsCategoryRepository newsCategoryRepository;

    @Override
    public void createKeyword(CreateKeywordRequest createKeywordRequest, User user) {
        if (keywordsRepository.existsByKeywordAndUserIdAndCategoryName(createKeywordRequest.getKeyword(), user.getId(), createKeywordRequest.getParentCategory())) {
            throw new BadRequestException("You have already created this keyword under " + createKeywordRequest.getParentCategory());
        }
        Optional<NewsCategory> optionalNewsCategory = newsCategoryRepository.findByCategoryName(createKeywordRequest.getParentCategory());
        optionalNewsCategory.ifPresentOrElse(newsCategory -> {
            Keyword keyword = new Keyword();
            keyword.setKeyword(createKeywordRequest.getKeyword());
            keyword.setActive(true);
            if (keyword.getUsers() == null) {
                keyword.setUsers(new ArrayList<>());
            }
            keyword.getUsers().add(user);
            keyword.setParentCategory(newsCategory);
            keywordsRepository.save(keyword);
        }, () -> {
            throw new BadRequestException("News category does not exist");
        });
    }

    @Override
    public void toggleKeywordActiveStatus(Long keywordId, User user) {
        Keyword keyword = keywordsRepository.findByIdAndUserId(keywordId, user.getId())
                .orElseThrow(() -> new BadRequestException("Invalid keyword id"));

        keyword.setActive(!keyword.isActive());
        keywordsRepository.save(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KeywordResponse> getAllKeywordsOfUser(User user) {
        return user
                .getKeywords()
                .stream()
                .map(keyword -> {
                    KeywordResponse keywordResponse = new KeywordResponse();
                    keywordResponse.setEnabled(keyword.isActive());
                    keywordResponse.setKeyword(keyword.getKeyword());
                    keywordResponse.setParentCategory(keyword.getParentCategory().getCategoryName());
                    keywordResponse.setKeywordId(keyword.getId());
                    return keywordResponse;
                })
                .toList();
    }

}
