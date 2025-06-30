package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {

    boolean existsByCategoryName(String categoryName);

    Optional<NewsCategory> findByCategoryName(String categoryName);

    Set<NewsCategory> findAllByCategoryNameIn(Collection<String> categories);

    @Query(value = "SELECT n FROM NewsCategory n WHERE n.isHidden = TRUE")
    List<NewsCategory> findAllByIsHiddenTrue();

    @Query(value = "SELECT n FROM NewsCategory n WHERE n.isHidden = FALSE")
    List<NewsCategory> findAllByIsHiddenFalse();

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE NewsCategory nc SET nc.isHidden = FALSE WHERE LOWER(nc.categoryName) IN :categories AND nc.isHidden = TRUE")
    int unhideNews(Collection<String> categories);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "UPDATE NewsCategory nc SET nc.isHidden = TRUE WHERE LOWER(nc.categoryName) IN :categories AND nc.isHidden = FALSE")
    int hideNews(Collection<String> categories);

}
