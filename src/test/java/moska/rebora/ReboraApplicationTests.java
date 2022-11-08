package moska.rebora;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
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
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.PolicyRepository;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    @Autowired
    UserMovieRepository userMovieRepository;

    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    MainBannerRepository mainBannerRepository;

    @Test
    @Transactional
    public void createUser() {
        List<User> userList = new ArrayList<>();
        for (int i = 1000; i < 100000; i++) {
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
            userList.add(user);
        }
        userTestRepository.saveAll(userList);
    }

    @Test
    public void createRecruitment() {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 1; i <= 100000; i++) {
            Recruitment recruitment = recruitmentRepository.getRecruitmentById((long) i);
            Pageable pageable = PageRequest.of(i - 1, 100);
            List<User> userList = userTestRepository.getTestUserList(pageable);
            userList.forEach(u -> {
                UserRecruitment userRecruitment = UserRecruitment.builder()
                        .userRecruitmentYn(true)
                        .user(u)
                        .userRecruitmentWish(true)
                        .userRecruitmentPeople(1)
                        .recruitment(recruitment)
                        .build();

                userRecruitmentRepository.save(userRecruitment);

            });
            recruitment.updateRecruitmentPeople(100);
            recruitmentRepository.save(recruitment);
        }
    }


    @Test
    public void LetGo() {
        LocalDateTime now = LocalDateTime.now();
        User user = userTestRepository.findById(1L).get();
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(14L);

        UserRecruitment userRecruitment = UserRecruitment.builder()
                .userRecruitmentYn(true)
                .userRecruitmentPeople(1)
                .user(user)
                .userRecruitmentWish(true)
                .recruitment(recruitment)
                .build();

        userRecruitmentRepository.save(userRecruitment);
    }

    @Test
    public void recruitmentTest() {
        Movie movie = Movie.builder()
                .movieName("인셉션2")
                .movieImage("인셉션이미지2")
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
                .theaterMaxPeople(10)
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

    @Test
    public void createUserMovie() {

        UserMovie userMovie = UserMovie.builder()
                .userMovieWish(true)
                .build();

        userMovieRepository.save(userMovie);
    }

    @Test
    public void createUserRecruitment() {
        User user = userTestRepository.getUserByUserEmail("kkb0804@5dalant.net");
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(8L);

        UserRecruitment userRecruitment = UserRecruitment.builder()
                .userRecruitmentWish(true)
                .user(user)
                .userRecruitmentYn(false)
                .recruitment(recruitment)
                .build();

        userRecruitmentRepository.save(userRecruitment);
    }

    @Test
    public void createBanner() {
        for(int i = 9; i < 109; i++) {
            Recruitment recruitment = recruitmentRepository.getRecruitmentById((long) i);
            Banner banner = Banner.builder()
                    .bannerMainText("베너메인텍스트테스트"+i)
                    .bannerSubText("배너서브텍스트테스트"+i)
                    .recruitment(recruitment)
                    .build();

            bannerRepository.save(banner);
        }
    }

    @Test
    public void createMainBanner(){
        for(int i = 102; i < 110; i++) {
            Banner banner = bannerRepository.getReferenceById((long) i);
            MainBanner mainBanner = MainBanner.builder().banner(banner).build();

            mainBannerRepository.save(mainBanner);
        }
    }

}
