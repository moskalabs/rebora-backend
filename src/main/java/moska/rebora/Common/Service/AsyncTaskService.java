package moska.rebora.Common.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Notification.Entity.Notification;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static moska.rebora.Common.CommonConst.CURRENT_SERVER;
import static moska.rebora.Common.CommonConst.LOCALHOST;

@Service("asyncTaskService")
@Slf4j
public class AsyncTaskService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Async
    @Transactional
    public void createNotificationRecruitment(
            @Param("notificationSubject") String notificationSubject,
            @Param("notificationContent") String notificationContent,
            @Param("notificationKind") NotificationKind notificationKind,
            @Param("recruitmentId") Long recruitmentId,
            @Param("movieId") Long movieId
    ) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            ObjectMapper objectMapper = new ObjectMapper();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("notificationSubject", notificationSubject);
            params.add("notificationContent", notificationContent);
            params.add("notificationKind", notificationKind);
            params.add("recruitmentId", recruitmentId);
            params.add("movieId", movieId);
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(params, headers);
            String url = CURRENT_SERVER + "/api/common/createNotification";
            restTemplate.postForEntity(url, request, String.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
