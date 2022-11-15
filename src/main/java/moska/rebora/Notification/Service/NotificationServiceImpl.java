package moska.rebora.Notification.Service;

import moska.rebora.Enum.NotificationKind;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.Entity.Notification;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public void createNotificationRecruitment(
            @Param("notificationSubject") String notificationSubject,
            @Param("notificationContent") String notificationContent,
            @Param("notificationKind") NotificationKind notificationKind,
            @Param("recruitment") Recruitment recruitment,
            @Param("userList") List<User> userList
    ) {

        List<Notification> notificationList = new ArrayList<>();

        userList.forEach(u -> {
            Notification notification = Notification
                    .builder()
                    .notificationSubject(notificationSubject)
                    .notificationContent(notificationContent)
                    .notificationKind(notificationKind)
                    .recruitment(recruitment)
                    .user(u)
                    .build();

            notificationList.add(notification);
        });

        notificationRepository.saveAll(notificationList);
    }

    @Override
    public void createNotificationPayment(
            String notificationSubject,
            String notificationContent,
            NotificationKind notificationKind,
            Payment payment) {
    }

    @Override
    public String createNotificationContent(
            String movieName,
            LocalDateTime theaterStartDate,
            String theaterDay,
            String theaterBrand,
            String theaterCinema,
            String theaterName
    ) {

        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");

        stringBuilder
                .append(movieName)
                .append(" ")
                .append(theaterStartDate.format(formatter))
                .append(" (")
                .append(theaterDay)
                .append(") / ")
                .append(theaterBrand)
                .append(" ")
                .append(theaterCinema)
                .append(" ")
                .append(theaterName);

        return stringBuilder.toString();
    }
}
