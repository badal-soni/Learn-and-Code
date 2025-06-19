package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.request.CreateKeywordRequest;
import com.intimetec.newsaggregation.entity.Keyword;
import com.intimetec.newsaggregation.entity.NewsCategory;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.KeywordsRepository;
import com.intimetec.newsaggregation.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

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
        Optional<NewsCategory> optionalNewsCategory = newsCategoryRepository.findByCategoryName(createKeywordRequest.getKeyword());
        optionalNewsCategory.ifPresentOrElse(newsCategory -> {
            Keyword keyword = new Keyword();
            keyword.setKeyword(createKeywordRequest.getKeyword());
            keyword.setActive(true);
            keyword.getUsers().add(user);
            keyword.setParentCategory(newsCategory);
            keywordsRepository.save(keyword);
        }, () -> {
            throw new BadRequestException("News category does not exist");
        });
    }

    @Override
    public void activateKeyword(Long keywordId, User user) {
        Consumer<Keyword> consumer = (keyWord) -> keyWord.setActive(true);
        updateActiveStatus(
                keywordId,
                user,
                consumer
        );
    }

    @Override
    public void deactivateKeyword(Long keywordId, User user) {
        Consumer<Keyword> consumer = (keyWord) -> keyWord.setActive(false);
        updateActiveStatus(
                keywordId,
                user,
                consumer
        );
    }

    private void updateActiveStatus(
            Long keywordId,
            User user,
            Consumer<Keyword> consumer
    ) {
        Optional<Keyword> optionalKeyword = keywordsRepository.findByIdAndUserId(keywordId, user.getId());
        optionalKeyword.ifPresentOrElse(keyWord -> {
            consumer.accept(keyWord);
            keywordsRepository.save(keyWord);
        }, () -> {
            throw new BadRequestException("You have not created this keyword");
        });
    }

}
