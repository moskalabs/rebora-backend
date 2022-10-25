package moska.rebora;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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
        User user = User.builder()
                .userEmail("test@test.com")
                .password("1234")
                .userName("기봉")
                .userNickname("기봉김2")
                .userGrade(UserGrade.NORMAL)
                .userPushYn(true)
                .userUseYn(true)
                .build();

        Recruitment recruitment = Recruitment.builder()
                .recruitment_end_date(LocalDateTime.now())
                .recruitment_banner(false)
                .recruitment_expose_yn(true)
                .recruitment_introduce("hello")
                .recruitment_main_text("hellomain")
                .recruitment_status(RecruitmentStatus.CONFIRMATION)
                .recruitment_sub_text("hellosub").build();

        UserRecruitment userRecruitment = UserRecruitment.builder()
                .recruitment(recruitment)
                .user(user)
                .user_recruitment_people(2)
                .user_recruitment_wish(false)
                .user_recruitment_yn(true)
                .build();

        userTestRepository.save(user);
        recruitmentRepository.save(recruitment);
        userRecruitmentRepository.save(userRecruitment);
    }

    @Test
    @Transactional
    public void findRecruitment() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Optional<Recruitment> recruitmentOptional = recruitmentRepository.findById(5L);
        if (recruitmentOptional.isPresent()) {
            Recruitment recruitment = recruitmentOptional.get();
            List<UserRecruitment> userRecruitmentList = recruitment.getUserRecruitmentList();
            User user = userRecruitmentList.get(0).getUser();
            log.info("hello={}",user.getId());
        }

        stopWatch.stop();
        log.info("stopWatch={}",stopWatch.prettyPrint());
    }
}
