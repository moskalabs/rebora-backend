package moska.rebora.Notification.Repository;

import moska.rebora.Notification.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationCustom {

    Page<NotificationDto> getNotificationPage(Pageable pageable, String userEmail);
}
