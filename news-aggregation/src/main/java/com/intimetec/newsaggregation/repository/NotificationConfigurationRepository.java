package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.NotificationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface NotificationConfigurationRepository extends JpaRepository<NotificationConfiguration, Long> {

    // todo: after creating the user, populate all the data related to the categories
    @Query(value = "SELECT config FROM NotificationConfiguration config LEFT JOIN FETCH config.newsCategory category WHERE config.newsCategory.categoryName IN :categoryName AND config.user.id = :userId")
    List<NotificationConfiguration> findByUserAndNewsCategory(Long userId, Collection<String> categoryName);

    @Query(value = "SELECT config FROM NotificationConfiguration config LEFT JOIN FETCH config.newsCategory category WHERE config.isEnabled = true")
    List<NotificationConfiguration> findAllEnabled();

}
