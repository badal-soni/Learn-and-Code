package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.dto.response.KeywordResponse;
import com.intimetec.newsaggregation.entity.BlockedKeyword;
import com.intimetec.newsaggregation.entity.Keyword;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.BlockedKeywordRepository;
import com.intimetec.newsaggregation.repository.KeywordsRepository;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.service.KeywordService;
import com.intimetec.newsaggregation.util.CommonUtility;
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
    private final BlockedKeywordRepository blockedKeywordRepository;
    private final NewsRepository newsRepository;

    @Override
    public void createKeyword(CreateKeywordRequest createKeywordRequest, User user) {
        if (keywordsRepository.existsByKeywordAndUserIdAndCategoryName(createKeywordRequest.getKeyword(), user.getId(), createKeywordRequest.getParentCategory())) {
            throw new BadRequestException(Messages.KEYWORD_ALREADY_EXISTS);
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
            throw new BadRequestException(Messages.NEWS_CATEGORY_NOT_FOUND);
        });
    }

    @Override
    public void toggleKeywordActiveStatus(Long keywordId, User user) {
        Keyword keyword = keywordsRepository
                .findByIdAndUserId(keywordId, user.getId())
                .orElseThrow(() -> new BadRequestException(Messages.KEYWORD_NOT_FOUND));

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

    @Override
    @Transactional
    public void addBlockedKeyword(String keyword) {
        String lowerCasedKeyword = keyword.toLowerCase();
        if (blockedKeywordRepository.existsByBlockedKeyword(lowerCasedKeyword)) {
            throw new BadRequestException(Messages.KEYWORD_ALREADY_EXISTS);
        }
        BlockedKeyword blockedKeyword = new BlockedKeyword();
        blockedKeyword.setBlockedKeyword(keyword);
        blockedKeywordRepository.save(blockedKeyword);

        String pattern = CommonUtility.buildRegularExpressionForKeywordMatching(List.of(keyword));
        newsRepository.setIsHiddenTrue(pattern);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllBlockedKeywords() {
        return blockedKeywordRepository
                .findAll()
                .stream()
                .map(BlockedKeyword::getBlockedKeyword)
                .toList();
    }

    @Override
    @Transactional
    public void deleteBlockedKeyword(String keyword) {
        String lowerCasedKeyword = keyword.toLowerCase();
        Optional<BlockedKeyword> blockedKeywordOptional = blockedKeywordRepository.findByBlockedKeyword(lowerCasedKeyword);
        blockedKeywordOptional.ifPresentOrElse(blockedKeyword -> {
            blockedKeywordRepository.deleteByBlockedKeyword(blockedKeyword.getBlockedKeyword());
            String pattern = CommonUtility.buildRegularExpressionForKeywordMatching(List.of(keyword));
            newsRepository.setIsHiddenFalse(pattern);
        }, () -> {
            throw new BadRequestException(Messages.KEYWORD_NOT_FOUND);
        });
    }

}
