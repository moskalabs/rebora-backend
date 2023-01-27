package moska.rebora.Common.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Entity.Notification;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CommonServiceImpl implements CommonService {

    UserRepository userRepository;

    MovieRepository movieRepository;

    UserMovieRepository userMovieRepository;
    RecruitmentRepository recruitmentRepository;
    NotificationRepository notificationRepository;
    PushService pushService;

    @Override
    public void createNotification(String notificationSubject,
                                   String notificationContent,
                                   NotificationKind notificationKind,
                                   Long recruitmentId,
                                   Long movieId) {

        LocalDateTime now = LocalDateTime.now();

        List<User> userList = userMovieRepository.getUserListByMovie(movieId);
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(recruitmentId);

        List<Notification> notificationList = new ArrayList<>();

        userList.forEach(u -> {

            Notification notification = Notification
                    .builder()
                    .notificationSubject(notificationSubject)
                    .notificationContent(notificationContent)
                    .notificationKind(notificationKind)
                    .notificationReadYn(false)
                    .recruitment(recruitment)
                    .user(u)
                    .build();

            pushService.sendUserPush(u, notificationSubject, notificationContent);

            notificationList.add(notification);
        });

        notificationRepository.saveAll(notificationList);
    }
}
