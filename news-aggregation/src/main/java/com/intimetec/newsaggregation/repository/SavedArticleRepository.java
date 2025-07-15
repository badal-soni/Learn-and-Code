package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.SavedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SavedArticleRepository extends JpaRepository<SavedArticle, Long> {

    @Query(value = "SELECT (COUNT(s) > 0) FROM SavedArticle s JOIN s.newsArticle n JOIN s.savedBy u WHERE n.id = :newsId AND u.id = :userId")
    boolean existsByNewsArticleIdAndSavedById(Long newsId, Long userId);

    @Query(value = "SELECT news FROM SavedArticle news JOIN FETCH news.newsArticle n JOIN FETCH news.savedBy sb WHERE n.isHidden = FALSE AND sb.id = :userId")
    List<SavedArticle> findAllBySavedById(@Param("userId") Long userId);

    @Query(value = "SELECT s.id FROM SavedArticle s JOIN s.newsArticle n WHERE s.savedBy.id = :savedBy AND n.isHidden = FALSE")
    List<Long> findAllNewsIdsBySavedById(Long savedBy);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM SavedArticle sa WHERE sa.newsArticle.id = :newsId AND sa.savedBy.id = :userId")
    int deleteBySavedByAndNewsArticleId(Long userId, Long newsId);

}
