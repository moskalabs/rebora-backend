package moska.rebora;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Enum.PolicySubject;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.Entity.Policy;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.PolicyRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@SpringBootTest
class ReboraApplicationTests {

    @Autowired
    UserRepository userTestRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    TheaterRepository theaterRepository;

    @Autowired
    PolicyRepository policyRepository;

    @Test
    public void createUser() {

        for (int i = 101; i < 1000; i++) {
            User user = User.builder()
                    .userEmail("test" + i + "@test.com")
                    .userName("test" + i)
                    .userNickname("testNickname" + i)
                    .userImage("testImage" + i)
                    .userGrade(UserGrade.NORMAL)
                    .userUseYn(true)
                    .userPushYn(false)
                    .userPushKey("")
                    .build();

            userTestRepository.save(user);
        }
    }

    @Test
    public void createRecruitment() {
        LocalDateTime now = LocalDateTime.now();

        List<User> userList = userTestRepository.findAll();


        List<Recruitment> recruitmentList = recruitmentRepository.findAll();
        for (int i = 0; i < 10; i++) {
            Recruitment recruitment = recruitmentList.get(i);
            for (int j = 0; j < i * 100; j++) {

                UserRecruitment userRecruitment = UserRecruitment.builder()
                        .userRecruitmentYn(true)
                        .userRecruitmentWish(true)
                        .userRecruitmentPeople(1)
                        .user(userList.get(j))
                        .recruitment(recruitment)
                        .build();

                userRecruitmentRepository.save(userRecruitment);
            }
        }

//        for (int i = 1; i < 10; i++) {
//
//
//            recruitmentRepository.save(recruitment);
//        }


    }

    @Test
    public void LetGo() {
        LocalDateTime now = LocalDateTime.now();
        User user = userTestRepository.findById(1L).get();

        Recruitment recruitment = Recruitment.builder()
                .recruitmentEndDate(now)
                .recruitmentBanner(false)
                .recruitmentExposeYn(true)
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .build();

        UserRecruitment userRecruitment = UserRecruitment.builder()
                .userRecruitmentYn(true)
                .userRecruitmentWish(true)
                .userRecruitmentPeople(1)
                .user(user)
                .recruitment(recruitment)
                .build();

        recruitmentRepository.save(recruitment);
        userRecruitmentRepository.save(userRecruitment);
    }

    @Test
    public void recruitmentTest() {
        Movie movie = Movie.builder()
                .movieName("인셉션")
                .movieImage("인셉션이미지")
                .movieCategory("스릴러")
                .movieDetailLink("https://www.naver.com")
                .movieRating(MovieRating.ALL)
                .movieDirector("미야자키 하야호")
                .moviePopularCount(0)
                .build();

        movieRepository.save(movie);
    }

    @Test
    public void updateRecruitmentYn() {
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(1L);
        Movie movie = movieRepository.getMovieById(1L);
        recruitment.updateMovie(movie);
        recruitmentRepository.save(recruitment);
    }

    @Test
    public void createTheater() {
        LocalDateTime now = LocalDateTime.now();
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(1L);

        Theater theater = Theater.builder()
                .theaterDay("수")
                .theaterName("1관")
                .theaterStartDatetime(now)
                .theaterEndDatetime(now)
                .theaterPeople(10)
                .theaterMinPeople(10)
                .theaterCinemaName("영등포점")
                .theaterCinemaBrandName("CGV")
                .theaterRegion("서울")
                .recruitment(recruitment)
                .build();

        theaterRepository.save(theater);
    }

    @Test
    public void createPolicy() {
        Policy policy = Policy.builder()
                .policyContent("hello")
                .policySubject(PolicySubject.PRIVACY_POLICY)
                .build();

        policyRepository.save(policy);
    }
}
