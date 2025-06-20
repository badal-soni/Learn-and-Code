package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, NewsSearchRepository {

    List<News> findAllByPublishedAt(LocalDate date);

    List<News> findAllByPublishedAtBetween(LocalDate from, LocalDate to);

    List<News> findAllByCategoriesCategoryNameAndPublishedAtBetween(String categoryName, LocalDate from, LocalDate to);

}
