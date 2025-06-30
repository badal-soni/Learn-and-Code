package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.SavedArticle;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

    boolean existsByNewsArticleIdAndSavedById(Long newsId, Long userId);

    @Query(value = "SELECT news FROM SavedArticle news JOIN FETCH news.newsArticle n JOIN FETCH news.savedBy sb WHERE n.isHidden = FALSE AND sb.id = :userId")
    List<SavedArticle> findAllBySavedBy(@Param("userId") Long userId);

    int deleteBySavedByAndNewsArticleId(User savedBy, Long newsId);

}
