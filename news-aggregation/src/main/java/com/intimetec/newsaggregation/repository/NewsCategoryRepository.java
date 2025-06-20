package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

    boolean existsByCategoryName(String categoryName);

    Optional<NewsCategory> findByCategoryName(String categoryName);

}
