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
    }
}
