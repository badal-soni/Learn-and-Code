package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.NewsReadHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsReadHistoryRepository extends JpaRepository<NewsReadHistory, Long> {

    boolean existsByNewsIdAndReadById(Long newsId, Long userId);

}
