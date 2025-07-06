package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.BlockedKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BlockedKeywordRepository extends JpaRepository<BlockedKeyword, Long> {

    boolean existsByBlockedKeyword(String keyword);

    Optional<BlockedKeyword> findByBlockedKeyword(String keyword);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM BlockedKeyword b WHERE b.blockedKeyword = :blockedKeyword")
    void deleteByBlockedKeyword(String blockedKeyword);

}
