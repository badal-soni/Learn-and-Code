package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.dto.request.DateRangeFilterCriteria;
import com.intimetec.newsaggregation.entity.News;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsSearchRepository {

    List<News> searchNewsByCriteria(DateRangeFilterCriteria dateRangeFilterCriteria, Pageable page);

}
