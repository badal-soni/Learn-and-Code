package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.entity.NewsLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsLikesRepository extends JpaRepository<NewsLikes, Long> {

    boolean existsByNewsIdAndInteractionUserIdAndInteractionType(Long newsId, Long userId, NewsInteractionType newsInteractionType);

}
