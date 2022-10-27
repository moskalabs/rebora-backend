package moska.rebora;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@SpringBootTest
class ReboraApplicationTests {

    @Autowired
    UserTestRepository userTestRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Test
    public void LetGo() {
        LocalDateTime now = LocalDateTime.now();
        User user = userTestRepository.findById(55L).get();

        Recruitment recruitment = Recruitment.builder()
                .recruitmentEndDate(now)
                .recruitmentBanner(false)
                .recruitmentExposeYn(true)
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .build();

        UserRecruitment userRecruitment = UserRecruitment.builder()
                .user_recruitment_yn(true)
                .user_recruitment_wish(false)
                .user_recruitment_people(1)
                .user(user)
                .recruitment(recruitment)
                .build();

        recruitmentRepository.save(recruitment);
        userRecruitmentRepository.save(userRecruitment);
    }
}
