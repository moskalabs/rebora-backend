package moska.rebora;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Repository.CinemaRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class TheaterTest {

    @Autowired
    TheaterRepository theaterRepository;

    @Test
    @Transactional
    public void testJpaOptional(){

        Theater theater = theaterRepository.getTheaterById(56L);
        Recruitment recruitment = theater.getRecruitment();
        log.info("recruitmentEndDate={}", recruitment.getRecruitmentEndDate());

        Optional<Theater> optionalTheater = theaterRepository.findById(56L);
        Theater theater1 = optionalTheater.get();
        Recruitment recruitment1 = theater1.getRecruitment();
        log.info("recruitment1EndDate={}", recruitment1.getRecruitmentEndDate());

//        User user = userRecruitment.getUser();
//        log.info("userEmail={}", user.getUserEmail());
//
//        Optional<UserRecruitment> optionalUserRecruitment = userRecruitmentRepository.findById(4L);
//        UserRecruitment userRecruitment1 = optionalUserRecruitment.get();
//        User user1 = userRecruitment1.getUser();
//        log.info("userEmail={}", user1.getUserEmail());

    }
}
