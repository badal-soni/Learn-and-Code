package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.constant.NewsInteractionType;
import com.intimetec.newsaggregation.entity.NewsLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.TreeSet;

@Repository
public interface NewsLikesRepository extends JpaRepository<NewsLikes, Long> {

    Optional<NewsLikes> findByNewsIdAndInteractionUserIdAndInteractionType(Long newsId, Long userId, NewsInteractionType newsInteractionType);

    @Query(value = "SELECT nl.news.id FROM NewsLikes nl JOIN nl.news n JOIN n.categories c WHERE nl.interactionUser.id = :userId AND n.isHidden = FALSE AND c.isHidden = FALSE")
    TreeSet<Long> findAllLikedNewsIds(Long userId);

}
