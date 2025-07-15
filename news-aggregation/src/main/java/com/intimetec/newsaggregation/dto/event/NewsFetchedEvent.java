package com.intimetec.newsaggregation.dto.event;

import com.intimetec.newsaggregation.entity.News;
import lombok.Data;

import java.util.List;

@Data
public class NewsFetchedEvent {

    private List<News> fetchedNews;

}
