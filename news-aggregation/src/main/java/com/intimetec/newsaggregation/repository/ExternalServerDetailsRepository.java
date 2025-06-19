package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.ExternalServerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ExternalServerDetailsRepository extends JpaRepository<ExternalServerDetail, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE ExternalServerDetail s SET s.isActive = :isActive WHERE s.id = :serverId")
    int updateActiveStatus(long serverId, boolean isActive);

    Optional<ExternalServerDetail> findByServerName(String newsApi);
}
