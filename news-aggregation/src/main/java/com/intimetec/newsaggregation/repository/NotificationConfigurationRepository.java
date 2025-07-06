package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.NotificationConfiguration;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationConfigurationRepository extends JpaRepository<NotificationConfiguration, Long> {

    @Query(value = "SELECT config FROM NotificationConfiguration config LEFT JOIN FETCH config.newsCategory category WHERE config.isEnabled = true AND config.newsCategory.isHidden = FALSE")
    List<NotificationConfiguration> findAllEnabled();

    @Query(value = "SELECT config FROM NotificationConfiguration config LEFT JOIN FETCH config.newsCategory category WHERE config.user = :user AND config.isEnabled = true AND config.newsCategory.isHidden = FALSE")
    List<NotificationConfiguration> findAllByUserAndNewsCategoryIsHiddenFalse(@Param("user") User user);

    @Query(value = "SELECT config FROM NotificationConfiguration config LEFT JOIN FETCH config.newsCategory category WHERE config.user = :user")
    List<NotificationConfiguration> findAllByUser(@Param("user") User user);

}
