package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.dto.request.DateRangeFilterCriteria;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.*;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.repository.NewsCategoryRepository;
import com.intimetec.newsaggregation.repository.NewsLikesRepository;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.repository.SavedArticleRepository;
import com.intimetec.newsaggregation.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsLikesRepository newsLikesRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final SavedArticleRepository savedArticleRepository;

    @Override
    public void likeNews(Long newsId, User likedBy) {
        addNewsInteraction(newsId, likedBy, NewsInteractionType.LIKE);
    }

    @Override
    public void dislikeNews(Long newsId, User dislikedBy) {
        addNewsInteraction(newsId, dislikedBy, NewsInteractionType.DISLIKE);
    }

    private void addNewsInteraction(
            Long newsId,
            User user,
            NewsInteractionType newsInteractionType
    ) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isEmpty()) {
            throw new BadRequestException("News with id " + newsId + " does not exist");
        }
        if (newsLikesRepository.existsByNewsIdAndInteractionUserIdAndInteractionType(newsId, user.getId(), newsInteractionType)) {
            throw new BadRequestException("You have already " + newsInteractionType.getInteractionType() + " this news before");
        }
        NewsLikes like = new NewsLikes();
        like.setNews(optionalNews.get());
        like.setInteractionUser(user);
        like.setInteractionType(newsInteractionType);
        newsLikesRepository.save(like);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getHeadlines(ViewHeadlinesRequest viewHeadlinesRequest) {
        if (Objects.nonNull(viewHeadlinesRequest.getTo())) {
            if (Objects.isNull(viewHeadlinesRequest.getFrom())) {
                throw new BadRequestException("Providing only end range is not valid, please provide rate range or no range");
            }
            return mapNews(newsRepository.findAllByPublishedAtBetween(viewHeadlinesRequest.getFrom(), viewHeadlinesRequest.getTo()));
        }
        if (Objects.nonNull(viewHeadlinesRequest.getFrom())) {
            return mapNews(newsRepository.findAllByPublishedAt(viewHeadlinesRequest.getFrom()));
        }
        return mapNews(newsRepository.findAll());
    }

    private List<NewsResponse> mapNews(List<News> newsList) {
        return newsList
                .stream()
                .map(news -> NewsResponse.builder()
                        .headline(news.getHeadline())
                        .id(news.getId())
                        .url(news.getUrl())
                        .description(news.getDescription())
                        .categories(news.getCategories().stream().map(NewsCategory::getCategoryName).toList())
                        .source(news.getSource())
                        .build()
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getHeadlinesUnderCategory(String categoryName, ViewHeadlinesRequest viewHeadlinesRequest) {
        categoryName = categoryName.toLowerCase().trim();
        if (!newsCategoryRepository.existsByCategoryName(categoryName)) {
            throw new BadRequestException("Category: " + categoryName + " does not exist");
        }
        return mapNews(newsRepository.findAllByCategoriesCategoryNameAndPublishedAtBetween(categoryName, viewHeadlinesRequest.getFrom(), viewHeadlinesRequest.getTo()));
    }

    @Override
    public void saveNews(Long newsId, User savedBy) {
        if (savedArticleRepository.existsByNewsArticleIdAndSavedById(newsId, savedBy.getId())) {
            throw new BadRequestException("You have already saved this news before");
        }

        Optional<News> optionalNews = newsRepository.findById(newsId);
        optionalNews.ifPresentOrElse(news -> {
            SavedArticle savedArticle = new SavedArticle();
            savedArticle.setNewsArticle(news);
            savedArticle.setSavedBy(savedBy);
            savedArticleRepository.save(savedArticle);
        }, () -> {
            throw new BadRequestException("News with id " + newsId + " does not exist");
        });
    }

    @Override
    public void unSaveNews(Long newsId, User savedBy) {
        if (!savedArticleRepository.existsByNewsArticleIdAndSavedById(savedBy.getId(), newsId)) {
            throw new BadRequestException("You have not saved this news before");
        }
        savedArticleRepository.deleteBySavedByAndNewsArticleId(savedBy, newsId);
    }

    @Override
    public List<NewsResponse> searchNews(DateRangeFilterCriteria dateRangeFilterCriteria) {
        /*
        todo: search by queryText (search in headline, description)
              filter by date (publishedAt or createdAt)
              sort by no. of likes and dislikes
         */
        Pageable pageable = PageRequest.of(dateRangeFilterCriteria.getPageIndex(), dateRangeFilterCriteria.getPageSize());
        return mapNews(newsRepository.searchNewsByCriteria(dateRangeFilterCriteria, pageable));
    }

}
