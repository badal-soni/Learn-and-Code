package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.NewsReadHistory;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsReadHistoryRepository extends JpaRepository<NewsReadHistory, Long> {

    boolean existsByNewsIdAndReadById(Long newsId, Long userId);

    @Query(value = "SELECT nr.id FROM NewsReadHistory nr JOIN nr.news n WHERE nr.readBy = ?1 AND n.isHidden = FALSE")
    List<Long> findAllIdsByReadBy(User readBy);

}
