package com.intimetec.newsaggregation.repository;

import com.intimetec.newsaggregation.entity.Notification;
import com.intimetec.newsaggregation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT n FROM Notification n WHERE n.receiver = :receiver AND n.hasRead = FALSE")
    List<Notification> findAllUnReadNotificationsByReceiver(User receiver);

    Optional<Notification> findByIdAndReceiver(Long notificationId, User receiver);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Notification n SET n.hasRead = :hasRead WHERE n.id IN :notificationIds")
    int updateReadStatus(Collection<Long> notificationIds, boolean hasRead);

}
