package com.intimetec.newsaggregation.worker;

import com.intimetec.newsaggregation.adapter.news.NewsFetcher;
import com.intimetec.newsaggregation.dto.event.NewsFetchedEvent;
import com.intimetec.newsaggregation.entity.News;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsScheduler {

    private final List<NewsFetcher> newsFetchers;
    private final ApplicationEventPublisher eventPublisher;

    private static final long THREE_HOURS_DELAY = 3 * 60 * 60 * 1000;

    @Scheduled(fixedDelay = THREE_HOURS_DELAY)
    @Async("asyncExecutor")
    public void fetchNewsFromExternalDataSource() {
        List<News> newsApiResponses = this.fetchNews();
        if (newsApiResponses.isEmpty()) {
            return;
        }

        List<News> nonHiddenNews = this.filterNonHiddenNews(newsApiResponses);
        NewsFetchedEvent newsFetchedEvent = new NewsFetchedEvent();
        newsFetchedEvent.setFetchedNews(nonHiddenNews);
        log.debug("Publishing news fetched event");
        eventPublisher.publishEvent(newsFetchedEvent);
    }

    private List<News> fetchNews() {
        for (NewsFetcher newsFetcher : newsFetchers) {
            List<News> newsApiResponses = newsFetcher.fetchNews();
            if (Objects.nonNull(newsApiResponses) && !newsApiResponses.isEmpty()) {
                log.info("Got data from {}", newsFetcher.getServiceName());
                return newsApiResponses;
            }
        }
        log.error("Cannot fetch news from any of the news sources");
        return List.of();
    }

    private List<News> filterNonHiddenNews(List<News> newsApiResponses) {
        return newsApiResponses
                .stream()
                .filter(news -> !news.getIsHidden())
                .toList();
    }

}
