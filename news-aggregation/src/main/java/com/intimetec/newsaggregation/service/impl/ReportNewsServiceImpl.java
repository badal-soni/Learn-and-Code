package com.intimetec.newsaggregation.service.impl;

import com.intimetec.newsaggregation.dto.Keywords;
import com.intimetec.newsaggregation.dto.NewsIds;
import com.intimetec.newsaggregation.dto.event.NewsReportedEvent;
import com.intimetec.newsaggregation.dto.request.ReportNewsArticleRequest;
import com.intimetec.newsaggregation.dto.response.ReportedNewsResponse;
import com.intimetec.newsaggregation.entity.News;
import com.intimetec.newsaggregation.entity.ReportedNews;
import com.intimetec.newsaggregation.entity.User;
import com.intimetec.newsaggregation.exception.BadRequestException;
import com.intimetec.newsaggregation.mapper.NewsMapper;
import com.intimetec.newsaggregation.repository.NewsRepository;
import com.intimetec.newsaggregation.repository.ReportedNewsRepository;
import com.intimetec.newsaggregation.service.ReportNewsService;
import com.intimetec.newsaggregation.util.CommonUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReportNewsServiceImpl implements ReportNewsService {

    private final ReportedNewsRepository reportedNewsRepository;
    private final NewsRepository newsRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${report.threshold.value}")
    private int reportThresholdValue;

    @Override
    public void reportNews(Long newsId, ReportNewsArticleRequest reportNewsArticleRequest, User reportedBy) {
        if (reportedNewsRepository.existsByReportedNewsIdAndReportedBy(newsId, reportedBy)) {
            throw new BadRequestException("You have already reported this news before");
        }
        final News news = newsRepository
                .findById(newsId)
                .orElseThrow(() -> new BadRequestException("News with id " + newsId + " does not exist"));

        ReportedNews reportedNews = new ReportedNews();
        reportedNews.setReportedBy(reportedBy);
        reportedNews.setReportReason(reportNewsArticleRequest.getReportReason());
        reportedNews.setReportedNews(news);
        publishNewsReportedEvent(reportedNewsRepository.saveAndFlush(reportedNews));
        final List<ReportedNews> reportedNewsList = news.getReportedNews();
        if (Objects.nonNull(reportedNewsList) && !reportedNewsList.isEmpty() && reportedNewsList.size() > reportThresholdValue) {
            newsRepository.updateHiddenStatusByIds(true, List.of(news.getId()));
        }
    }

    private void publishNewsReportedEvent(ReportedNews reportedNews) {
        NewsReportedEvent newsReportedEvent = new NewsReportedEvent();
        newsReportedEvent.setNewsId(reportedNews.getReportedNews().getId());
        newsReportedEvent.setNewsUrl(reportedNews.getReportedNews().getUrl());
        newsReportedEvent.setReportReason(reportedNews.getReportReason());
        newsReportedEvent.setReportedByEmail(reportedNews.getReportedBy().getEmail());
        eventPublisher.publishEvent(newsReportedEvent);
    }

    @Override
    public void hideNews(NewsIds newsIds) {
        newsRepository.updateHiddenStatusByIds(true, newsIds.getNewsIds());
    }

    @Override
    public void unHideNews(NewsIds newsIds) {
        newsRepository.updateHiddenStatusByIds(false, newsIds.getNewsIds());
    }

    @Override
    public void hideByKeywords(Keywords keywords) {
        final String regexpPattern = CommonUtility.buildRegularExpressionForKeywordMatching(keywords.getKeywords());
        newsRepository.updateHiddenStatusByKeywords(true, regexpPattern);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportedNewsResponse> getAllReportedNews() {
        return NewsMapper.mapToReportedNewsResponse(reportedNewsRepository.findAll());
    }

}
