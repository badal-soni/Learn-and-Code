package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.ReportedNews;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportedNewsRepository extends JpaRepository<ReportedNews, Long> {

    boolean existsByReportedNewsIdAndReportedByAndReportedNewsIsHiddenFalse(Long newsId, User reportedBy);

}
