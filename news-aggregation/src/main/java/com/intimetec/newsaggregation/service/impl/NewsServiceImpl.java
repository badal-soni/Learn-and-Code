package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.constant.Messages;
import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.entity.*;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.mapper.NewsMapper;
import com.intimetec.newsaggregation.repository.*;
import com.intimetec.newsaggregation.service.NewsPreferencesService;
import com.intimetec.newsaggregation.service.NewsService;
import com.intimetec.newsaggregation.util.CommonUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
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
    private final NewsReadHistoryRepository newsReadHistoryRepository;
    private final NewsPreferencesService newsPreferencesService;

    @Override
    public void toggleLikeStatus(Long newsId, User likedBy) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isEmpty()) {
            throw new BadRequestException(String.format(Messages.NEWS_ID_NOT_FOUND, newsId));
        }
        final NewsInteractionType likeNews = NewsInteractionType.LIKE;
        Optional<NewsLikes> like = newsLikesRepository.findByNewsIdAndInteractionUserIdAndInteractionType(
                newsId,
                likedBy.getId(),
                likeNews
        );
        like.ifPresentOrElse(newsLike -> {
            newsLikesRepository.deleteById(newsLike.getId());
        }, () -> {
            NewsLikes newLike = new NewsLikes();
            newLike.setNews(optionalNews.get());
            newLike.setInteractionUser(likedBy);
            newLike.setInteractionType(likeNews);
            newsLikesRepository.save(newLike);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getHeadlines(User currentUser, ViewHeadlinesRequest viewHeadlinesRequest) {
        List<News> newsList;
        if (Objects.nonNull(viewHeadlinesRequest.getTo())) {
            if (Objects.isNull(viewHeadlinesRequest.getFrom())) {
                throw new BadRequestException(Messages.INVALID_DATE_RANGE);
            }
            if (viewHeadlinesRequest.getFrom().isAfter(viewHeadlinesRequest.getTo())) {
                throw new BadRequestException(Messages.INVALID_DATE_RANGE);
            }
            newsList = newsRepository.findAllByPublishedAtBetweenAndIsHiddenFalse(viewHeadlinesRequest.getFrom(), viewHeadlinesRequest.getTo());
            return newsPreferencesService.sortNewsByUserPreferences(currentUser, newsList);
        }
        if (Objects.nonNull(viewHeadlinesRequest.getFrom())) {
            newsList = newsRepository.findAllByPublishedAtAndIsHiddenFalse(viewHeadlinesRequest.getFrom());
            return newsPreferencesService.sortNewsByUserPreferences(currentUser, newsList);
        }
        newsList = newsRepository.findAllByIsHiddenFalse();
        return newsPreferencesService.sortNewsByUserPreferences(currentUser, newsList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getHeadlinesUnderCategory(
            User user,
            String categoryName,
            ViewHeadlinesRequest viewHeadlinesRequest
    ) {
        if (!newsCategoryRepository.existsByCategoryName(categoryName)) {
            throw new BadRequestException(Messages.NEWS_CATEGORY_NOT_FOUND);
        }
        final LocalDate from = viewHeadlinesRequest.getFrom();
        final LocalDate to = viewHeadlinesRequest.getTo();
        List<News> newsList;
        if (Objects.isNull(from) && Objects.isNull(to)) {
            newsList = newsRepository.findAllByCategory(categoryName);
        } else if (Objects.nonNull(from) && Objects.isNull(to)) {
            newsList = newsRepository.findAllByCategoryFromPublishedAt(categoryName, from);
        } else if (Objects.isNull(from)) {
            newsList = newsRepository.findAllByCategoryTillPublishedAt(categoryName, to);
        } else {
            newsList = newsRepository.findAllByCategoriesCategoryNameAndPublishedAtBetweenAndIsHiddenFalse(categoryName, viewHeadlinesRequest.getFrom(), viewHeadlinesRequest.getTo());
        }
        return newsPreferencesService.sortCategoryNews(user, newsList);
    }

    @Override
    public void saveNews(Long newsId, User savedBy) {
        if (savedArticleRepository.existsByNewsArticleIdAndSavedById(newsId, savedBy.getId())) {
            throw new BadRequestException(Messages.NEWS_ALREADY_SAVED);
        }

        newsRepository
                .findById(newsId)
                .ifPresentOrElse(news -> {
                    SavedArticle savedArticle = new SavedArticle();
                    savedArticle.setNewsArticle(news);
                    savedArticle.setSavedBy(savedBy);
                    savedArticleRepository.save(savedArticle);
                }, () -> {
                    throw new BadRequestException(String.format(Messages.NEWS_ID_NOT_FOUND, newsId));
                });
    }

    @Override
    public void unSaveNews(Long newsId, User savedBy) {
        if (!savedArticleRepository.existsByNewsArticleIdAndSavedById(newsId, savedBy.getId())) {
            throw new BadRequestException(Messages.NEWS_NOT_SAVED);
        }
        savedArticleRepository.deleteBySavedByAndNewsArticleId(savedBy.getId(), newsId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getTodayHeadline(User currentUser) {
        final List<News> newsList = newsRepository.findAllByPublishedAtAndIsHiddenFalse(LocalDate.now());
        return newsPreferencesService.sortNewsByUserPreferences(currentUser, newsList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> searchNews(SearchNewsRequest searchNewsRequest) {
        List<News> newsList = getNews(searchNewsRequest);
        if (newsList.isEmpty()) {
            return List.of();
        }
        newsList = filterNonHiddenNews(newsList);
        List<News> sortedList;
        if (Objects.isNull(searchNewsRequest.getIsAscendingOrder())) {
            return NewsMapper.mapToNewsResponse(newsList);
        }
        if (searchNewsRequest.getIsAscendingOrder()) {
            sortedList = newsList
                    .stream()
                    .sorted(Comparator.comparingInt(news -> news.getUserInteractions().size()))
                    .toList();
        } else {
            sortedList = newsList
                    .stream()
                    .sorted((news1, news2) -> news2.getUserInteractions().size() - news1.getUserInteractions().size())
                    .toList();
        }
        return NewsMapper.mapToNewsResponse(sortedList);
    }

    private List<News> getNews(SearchNewsRequest searchNewsRequest) {
        final String textToSearch = searchNewsRequest.getSearchQuery();
        final LocalDate fromDate = searchNewsRequest.getFrom();
        final LocalDate toDate = searchNewsRequest.getTo();

        if (Objects.isNull(fromDate) && Objects.isNull(toDate)) {
            return newsRepository.findAllNewsMatchesHeadlineOrDescriptionAndHiddenFalse(textToSearch);
        } else if (Objects.nonNull(fromDate) && Objects.isNull(toDate)) {
            return newsRepository.findAllNewsMatchesHeadlineOrDescriptionFromPublishedDate(textToSearch, fromDate);
        } else if (Objects.isNull(fromDate)) {
            return newsRepository.findAllNewsMatchesHeadlineOrDescriptionTillPublishedDate(textToSearch, toDate);
        } else {
            return newsRepository.findAllNewsMatchesHeadlineOrDescriptionAndPublishedAtInBetween(textToSearch, fromDate, toDate);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getHiddenNews() {
        return NewsMapper.mapToNewsResponse(newsRepository.findAllByIsHiddenTrue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getNewsWithKeywords(Keywords keywords) {
        final String pattern = CommonUtility.buildRegularExpressionForKeywordMatching(keywords.getKeywords());
        final List<News> newsList = filterNonHiddenNews(newsRepository.findAllHavingKeywords(pattern));
        return NewsMapper.mapToNewsResponse(newsList);
    }

    @Override
    public NewsResponse getNewsById(Long newsId, User user) {
        News news = newsRepository
                .findById(newsId)
                .orElseThrow(() -> new BadRequestException(String.format(Messages.NEWS_ID_NOT_FOUND, newsId)));
        if (!newsReadHistoryRepository.existsByNewsIdAndReadById(newsId, user.getId())) {
            NewsReadHistory newsHistory = new NewsReadHistory();
            newsHistory.setNews(news);
            newsHistory.setReadBy(user);
            newsReadHistoryRepository.save(newsHistory);
        }
        return NewsMapper.mapToNewsResponse(List.of(news)).get(0);
    }

    private List<News> filterNonHiddenNews(List<News> newsList) {
        return newsList
                .stream()
                .filter(
                        news -> news
                                .getCategories()
                                .stream()
                                .noneMatch(NewsCategory::isHidden)
                )
                .toList();
    }

}
