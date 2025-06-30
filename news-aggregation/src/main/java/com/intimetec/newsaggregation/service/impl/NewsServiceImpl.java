package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.request.SearchNewsRequest;
import com.intimetec.newsaggregation.dto.request.ViewHeadlinesRequest;
import com.intimetec.newsaggregation.dto.response.NewsResponse;
import com.intimetec.newsaggregation.dto.response.UserLikedNewsResponse;
import com.intimetec.newsaggregation.entity.*;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.mapper.NewsMapper;
import com.intimetec.newsaggregation.repository.*;
import com.intimetec.newsaggregation.service.NewsService;
import com.intimetec.newsaggregation.util.CommonUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsLikesRepository newsLikesRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final SavedArticleRepository savedArticleRepository;
    private final NewsReadHistoryRepository newsReadHistoryRepository;

    @Override
    public void toggleLikeStatus(Long newsId, User likedBy) {
        Optional<News> optionalNews = newsRepository.findById(newsId);
        if (optionalNews.isEmpty()) {
            throw new BadRequestException("News with id " + newsId + " does not exist");
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
    public List<NewsResponse> getHeadlines(ViewHeadlinesRequest viewHeadlinesRequest) {
        if (Objects.nonNull(viewHeadlinesRequest.getTo())) {
            if (Objects.isNull(viewHeadlinesRequest.getFrom())) {
                throw new BadRequestException("Providing only end range is not valid, please provide rate range or no range");
            }
            return NewsMapper.mapToNewsResponse(newsRepository.findAllByPublishedAtBetweenAndIsHiddenFalse(viewHeadlinesRequest.getFrom(), viewHeadlinesRequest.getTo()));
        }
        if (Objects.nonNull(viewHeadlinesRequest.getFrom())) {
            return NewsMapper.mapToNewsResponse(newsRepository.findAllByPublishedAtAndIsHiddenFalse(viewHeadlinesRequest.getFrom()));
        }
        return NewsMapper.mapToNewsResponse(newsRepository.findAllByIsHiddenFalse());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> getHeadlinesUnderCategory(String categoryName, ViewHeadlinesRequest viewHeadlinesRequest) {
        if (!newsCategoryRepository.existsByCategoryName(categoryName)) {
            throw new BadRequestException("Category: " + categoryName + " does not exist");
        }
        final LocalDate from = viewHeadlinesRequest.getFrom();
        final LocalDate to = viewHeadlinesRequest.getTo();
        if (Objects.isNull(from) && Objects.isNull(to)) {
            return NewsMapper.mapToNewsResponse(newsRepository.findAllByCategory(categoryName));
        } else if (Objects.nonNull(from) && Objects.isNull(to)) {
            return NewsMapper.mapToNewsResponse(newsRepository.findAllByCategoryFromPublishedAt(categoryName, from));
        } else if (Objects.isNull(from)) {
            return NewsMapper.mapToNewsResponse(newsRepository.findAllByCategoryTillPublishedAt(categoryName, to));
        }
        return NewsMapper.mapToNewsResponse(newsRepository.findAllByCategoriesCategoryNameAndPublishedAtBetweenAndIsHiddenFalse(categoryName, viewHeadlinesRequest.getFrom(), viewHeadlinesRequest.getTo()));
    }

    @Override
    public void saveNews(Long newsId, User savedBy) {
        if (savedArticleRepository.existsByNewsArticleIdAndSavedById(newsId, savedBy.getId())) {
            throw new BadRequestException("You have already saved this news before");
        }

        newsRepository
                .findById(newsId)
                .ifPresentOrElse(news -> {
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
    @Transactional(readOnly = true)
    public List<NewsResponse> getTodayHeadline() {
        return NewsMapper.mapToNewsResponse(newsRepository.findAllByPublishedAtAndIsHiddenFalse(LocalDate.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewsResponse> searchNews(SearchNewsRequest searchNewsRequest) {
        final List<News> newsList = getNews(searchNewsRequest);
        if (newsList.isEmpty()) {
            return List.of();
        }
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
        return NewsMapper.mapToNewsResponse(newsRepository.findAllHavingKeywords(pattern));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLikedNewsResponse> getAllNewsWithUserLikedStatus(User user) {
        List<Long> allNewsIds = newsRepository.findAllIds();
        Set<Long> newsIdsLikedByUser = newsLikesRepository.findAllLikedNewsIds(user.getId());
        List<UserLikedNewsResponse> userLikedNewsResponses = new ArrayList<>();

        allNewsIds.forEach(newsId -> {
            boolean isLiked = newsIdsLikedByUser.contains(newsId);
            UserLikedNewsResponse userLikedNewsResponse = new UserLikedNewsResponse();
            userLikedNewsResponse.setNewsId(newsId);
            userLikedNewsResponse.setLiked(isLiked);
            userLikedNewsResponses.add(userLikedNewsResponse);
        });
        return userLikedNewsResponses;
    }

    @Override
    public NewsResponse getNewsById(Long newsId, User user) {
        News news = newsRepository
                .findById(newsId)
                .orElseThrow(() -> new BadRequestException("News with id: " + newsId + " does not exists"));
        if (!newsReadHistoryRepository.existsByNewsIdAndReadById(newsId, user.getId())) {
            NewsReadHistory newsHistory = new NewsReadHistory();
            newsHistory.setNews(news);
            newsHistory.setReadBy(user);
            newsReadHistoryRepository.save(newsHistory);
        }
        return NewsMapper.mapToNewsResponse(List.of(news)).get(0);
    }

}
