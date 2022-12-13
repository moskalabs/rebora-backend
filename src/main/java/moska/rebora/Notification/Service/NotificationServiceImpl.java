package moska.rebora.Notification.Service;

import moska.rebora.Common.Service.AsyncTaskService;
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
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Resource(name = "asyncTaskService")
    AsyncTaskService asyncTaskService;

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
    @Transactional
    public Page<NotificationDto> getNotificationList(Pageable pageable, String userEmail) {
        Page<NotificationDto> notificationDtoPage = notificationRepository.getNotificationPage(pageable, userEmail);
        List<NotificationDto> notificationDtoList = notificationDtoPage.getContent();

        List<Long> readNotificationIdList = notificationDtoList.stream().map(NotificationDto::getNotificationId).collect(Collectors.toList());
        ;
        notificationRepository.readNotifications(readNotificationIdList);

        return notificationDtoPage;
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

    @Override
    public void createPaymentEndNotification(Recruitment recruitment, Theater theater, User user, Movie movie, Boolean paymentEndYn) {
        String content = createNotificationContent(
                movie.getMovieName(),
                theater.getTheaterStartDatetime(),
                theater.getTheaterDay(),
                theater.getTheaterCinemaBrandName(),
                theater.getTheaterCinemaName(),
                theater.getTheaterName()
        );
        String subject = "모집 신청한 " + movie.getMovieName() + "의 결재가" + (paymentEndYn ? "완료되었습니다." : "실패했습니다");
        Notification notification = Notification
                .builder()
                .notificationSubject(subject)
                .notificationContent(content)
                .notificationKind(NotificationKind.CONFORMATION)
                .recruitment(recruitment)
                .user(user)
                .build();

        notificationRepository.save(notification);
    }
}
