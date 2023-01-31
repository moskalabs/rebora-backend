package moska.rebora.Notification.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Common.Service.AsyncTaskService;
import moska.rebora.Common.Service.PushService;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.Entity.Notification;
import moska.rebora.Notification.NotificationDto;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;

    PushService pushService;

    @Override
    public void createNotificationRecruitment(
            @Param("movieName") String movieName,
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
                    .movieName(movieName)
                    .notificationReadYn(false)
                    .user(u)
                    .build();

            notificationList.add(notification);

            pushService.sendUserPush(u, notificationSubject, movieName + " " + notificationContent);
        });

        notificationRepository.saveAll(notificationList);
    }

    @Override
    public void createNotificationRecruitment(
            String notificationSubject,
            String notificationContent,
            NotificationKind notificationKind,
            Recruitment recruitment,
            String movieName,
            User user) {

        Notification notification = Notification
                .builder()
                .notificationSubject(notificationSubject)
                .notificationContent(notificationContent)
                .notificationKind(notificationKind)
                .recruitment(recruitment)
                .notificationReadYn(false)
                .user(user)
                .build();

        pushService.sendUserPush(user, notificationSubject, movieName + " " + notificationContent);

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Page<NotificationDto> getNotificationList(Pageable pageable, String userEmail) {
        Page<NotificationDto> notificationDtoPage = notificationRepository.getNotificationPage(pageable, userEmail);
        List<NotificationDto> notificationDtoList = notificationDtoPage.getContent();

        List<Long> readNotificationIdList = notificationDtoList.stream().map(NotificationDto::getNotificationId).collect(Collectors.toList());

        notificationRepository.readNotifications(readNotificationIdList);

        return notificationDtoPage;
    }

    @Override
    public void createNotificationPayment(
            String notificationSubject,
            String notificationContent,
            NotificationKind notificationKind,
            String movieName,
            User user,
            Recruitment recruitment,
            Payment payment
    ) {

        Notification notification = Notification
                .builder()
                .notificationSubject(notificationSubject)
                .notificationContent(notificationContent)
                .notificationKind(notificationKind)
                .recruitment(recruitment)
                .movieName(movieName)
                .notificationReadYn(false)
                .user(user)
                .payment(payment)
                .build();

        pushService.sendUserPush(user, notificationSubject, movieName + " " + notificationContent);

        notificationRepository.save(notification);
    }

    @Override
    public String createNotificationContent(
            LocalDateTime theaterStartDate,
            String theaterDay,
            String theaterBrand,
            String theaterCinema,
            String theaterName
    ) {

        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 dd일");

        stringBuilder
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

    @Override
    public void createPaymentEndNotification(Recruitment recruitment, Theater theater, User user, Movie movie, Payment payment, Boolean paymentEndYn) {

        String content = createNotificationContent(
                theater.getTheaterStartDatetime(),
                theater.getTheaterDay(),
                theater.getTheaterCinemaBrandName(),
                theater.getTheaterCinemaName(),
                theater.getTheaterName()
        );

        String subject = "모집 신청한 " + movie.getMovieName() + "의 결재가" + (paymentEndYn ? "완료되었습니다." : "실패했습니다");
        Notification notification = Notification
                .builder()
                .movieName(movie.getMovieName())
                .notificationSubject(subject)
                .notificationContent(content)
                .notificationKind(NotificationKind.CONFORMATION)
                .recruitment(recruitment)
                .notificationReadYn(false)
                .user(user)
                .payment(payment)
                .build();

        pushService.sendUserPush(user, subject, movie.getMovieName() + " " + content);

        notificationRepository.save(notification);
    }
}
