package moska.rebora.Common.Service;

import moska.rebora.Enum.NotificationKind;
import org.springframework.data.repository.query.Param;

public interface CommonService {

    void createNotification(@Param("notificationSubject") String notificationSubject,
                            @Param("notificationContent") String notificationContent,
                            @Param("notificationKind") NotificationKind notificationKind,
                            @Param("recruitmentId") Long recruitmentId,
                            @Param("movieId") Long movieId);
}
