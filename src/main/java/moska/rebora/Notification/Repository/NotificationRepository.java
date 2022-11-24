package moska.rebora.Notification.Repository;

import moska.rebora.Notification.Entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustom {

    List<Notification> getNotificationsByIdIn(List<Long> notificationIdList);

    Integer countNotificationByNotificationReadYnFalseAndUserUserEmail(String userEmail);

    @Modifying
    @Query("update Notification n set n.notificationReadYn = true where n.id IN (:notificationList)")
    void readNotifications(List<Long> notificationList);
}
