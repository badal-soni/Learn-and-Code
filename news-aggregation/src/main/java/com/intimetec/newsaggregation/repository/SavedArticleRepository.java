package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.SavedArticle;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

    boolean existsByNewsArticleIdAndSavedById(Long newsId, Long userId);

    List<SavedArticle> findAllBySavedBy(User savedBy);

    int deleteBySavedByAndNewsArticleId(User savedBy, Long newsId);

}
