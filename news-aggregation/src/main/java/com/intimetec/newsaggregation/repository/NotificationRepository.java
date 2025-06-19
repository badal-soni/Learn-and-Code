package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.Notification;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllNotificationsByReceiver(User user);

}
