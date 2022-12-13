package moska.rebora.Notification.Service;

import moska.rebora.Enum.NotificationKind;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.NotificationDto;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {

    void createNotificationRecruitment(
            @Param("notificationSubject") String notificationSubject,
            @Param("notificationContent") String notificationContent,
            @Param("notificationKind") NotificationKind notificationKind,
            @Param("recruitment") Recruitment recruitment,
            @Param("userList") List<User> userList
    );

    void createNotificationPayment(
            @Param("notificationSubject") String notificationSubject,
            @Param("notificationContent") String notificationContent,
            @Param("notificationKind") NotificationKind notificationKind,
            @Param("payment") Payment payment
    );

    String createNotificationContent(
            @Param("movieName") String movieName,
            @Param("theaterStartDate") LocalDateTime theaterStartDate,
            @Param("theaterDay") String theaterDay,
            @Param("theaterBrand") String theaterBrand,
            @Param("theaterCinema") String theaterCinema,
            @Param("theaterName") String theaterName
    );

    Page<NotificationDto> getNotificationList(
            @Param("pageable") Pageable pageable,
            @Param("userEmail") String userEmail
    );

    void createPaymentEndNotification(Recruitment recruitment,Theater theater, User user, Movie movie, Boolean paymentEndYn);
}
