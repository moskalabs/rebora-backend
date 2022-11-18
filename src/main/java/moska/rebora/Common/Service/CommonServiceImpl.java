package moska.rebora.Common.Service;

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

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserMovieRepository userMovieRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public void createNotification(String notificationSubject,
                                   String notificationContent,
                                   NotificationKind notificationKind,
                                   Long recruitmentId,
                                   Long movieId) {

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

            notificationList.add(notification);
        });

        notificationRepository.saveAll(notificationList);
    }
}
