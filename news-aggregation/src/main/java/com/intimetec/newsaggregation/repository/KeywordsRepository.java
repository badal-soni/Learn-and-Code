package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KeywordsRepository extends JpaRepository<Keyword, Long> {

    @Query("SELECT COUNT(k) > 0 FROM Keyword k JOIN k.users u JOIN k.parentCategory c WHERE k.keyword = :keyword AND u.id = :userId AND c.categoryName = :categoryName")
    boolean existsByKeywordAndUserIdAndCategoryName(String keyword, Long userId, String categoryName);

    @Query("SELECT k FROM Keyword k JOIN k.users u WHERE k.id = :keywordId AND u.id = :userId")
    Optional<Keyword> findByIdAndUserId(Long keywordId, Long id);
}
