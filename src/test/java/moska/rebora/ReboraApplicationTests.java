package moska.rebora;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Cinema.Entity.BrandMovie;
import moska.rebora.Cinema.Repository.BrandMovieRepository;
import moska.rebora.Cinema.Repository.BrandRepository;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Common.Repository.CategoryRepository;
import moska.rebora.Enum.*;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Entity.MovieCategory;
import moska.rebora.Movie.Repository.MovieCategoryRepository;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    @Autowired
    UserMovieRepository userMovieRepository;

    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    MainBannerRepository mainBannerRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    BrandMovieRepository brandMovieRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    MovieCategoryRepository movieCategoryRepository;

    @Autowired
    PaymentRepository paymentRepository;

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
    public void createMovieTest() {
        Movie movie = Movie.builder()
                .movieName("이웃집 토토로")
                .movieImage("https://rebora.s3.ap-northeast-2.amazonaws.com/default/movieDefaultImage.png")
                .movieDetailLink("https://movie.naver.com/movie/bi/mi/basic.naver?code=18781")
                .movieRating(MovieRating.ALL)
                .movieBannerImage("https://movie.naver.com/movie/bi/mi/basic.naver?code=18781")
                .movieRecruitmentImage("https://rebora.s3.ap-northeast-2.amazonaws.com/default/recruitmentDefaultImage.png")
                .movieDirector("미야자키 하야호")
                .movieRunningTime(87)
                .movieStarRating(90)
                .moviePopularCount(0)
                .build();
        movieRepository.save(movie);
    }

    @Test
    public void createCategoryMovie() {
        Movie movie = movieRepository.getMovieById(1L);
        Category categoryAnimation = categoryRepository.getReferenceById(9L);
        Category categoryFamily = categoryRepository.getReferenceById(4L);
        Category categoryFantasy = categoryRepository.getReferenceById(15L);

        List<MovieCategory> movieCategoryList = new ArrayList<>();

        MovieCategory movieCategory1 = MovieCategory
                .builder()
                .movie(movie)
                .category(categoryAnimation)
                .build();

        movieCategoryList.add(movieCategory1);

        MovieCategory movieCategory2 = MovieCategory
                .builder()
                .movie(movie)
                .category(categoryFamily)
                .build();

        movieCategoryList.add(movieCategory2);

        MovieCategory movieCategory3 = MovieCategory
                .builder()
                .movie(movie)
                .category(categoryFantasy)
                .build();

        movieCategoryList.add(movieCategory3);

        movieCategoryRepository.saveAll(movieCategoryList);
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

        Banner banner = Banner.builder()
                .bannerMainText("")
                .bannerSubText("")
                .bannerImage("https://rebora.s3.ap-northeast-2.amazonaws.com/default/bannerDefaultImage.png")
                .build();

        bannerRepository.save(banner);
    }

    @Test
    public void createMainBanner() {
        for (int i = 102; i < 110; i++) {
            Banner banner = bannerRepository.getReferenceById((long) i);
            MainBanner mainBanner = MainBanner.builder().banner(banner).build();

            mainBannerRepository.save(mainBanner);
        }
    }

    @Test
    public void createBrand() {
        log.info("date={}", LocalDateTime.now().minusDays(3).toLocalDate().atTime(LocalTime.MAX));
    }

    @Test
    public void createBrandMovie() {
        Movie movie = movieRepository.getMovieById(1L);
        Brand brand = brandRepository.getReferenceById(1L);
        BrandMovie brandMovie = BrandMovie
                .builder()
                .movie(movie)
                .brand(brand)
                .build();

        brandMovieRepository.save(brandMovie);
    }

    @Test
    public void createCategory() {
        Category category = Category
                .builder()
                .categoryName("스릴러")
                .build();

        categoryRepository.save(category);
    }

    @Test
    public void notiTest() {
        log.info("notification={}", notificationService.createNotificationContent(
                "이웃집 토토로",
                LocalDateTime.now(),
                "수",
                "메가박스",
                "영등포점",
                "2관"
        ));
    }

    @Test
    public void userRecruitmentTest() {
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(1L);

        List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getUserRecruitmentByRecruitment(recruitment);

        for (UserRecruitment userRecruitment : userRecruitmentList) {
            log.info(userRecruitment.getUser().getUserEmail());
        }
    }

    @Test
    @Transactional
    public void paymentTest() {

        UserRecruitment userRecruitment = userRecruitmentRepository.findById(3000004L).get();

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(1L);
        stringBuilder.append("_");
        stringBuilder.append(3L);
        stringBuilder.append("_");
        stringBuilder.append(now);


        Payment payment = Payment.builder()
                .id(stringBuilder.toString())
                .paymentKey("paymentKey")
                .paymentContent("예약 결제")
                .paymentAmount(7000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentStatus(PaymentStatus.COMPLETE)
                .userRecruitment(userRecruitment)
                .build();

        userRecruitment.updatePayment(payment);
        paymentRepository.save(payment);
        userRecruitmentRepository.save(userRecruitment);
    }

    @Test
    void getBannerList() {
        bannerRepository.getCompareBannerList();
    }

    @Test
    void createas() {
        Movie movie = movieRepository.getMovieById(1L);
        Brand brand = brandRepository.getReferenceById(1L);
        BrandMovie brandMovie = BrandMovie
                .builder()
                .movie(movie)
                .brand(brand)
                .build();

        brandMovieRepository.save(brandMovie);
    }

    @Test
    void createMovieCategory() {
        Movie movie = movieRepository.getMovieByMovieName("해리 포터와 불사조 기사단");
        String categoryString = "판타지, 모험, 미스터리".replaceAll(" ", "");
        String[] categoryList = categoryString.split(",");
        List<MovieCategory> movieCategoryList = new ArrayList<>();

        for (String s : categoryList) {
            MovieCategory movieCategory = MovieCategory
                    .builder()
                    .movie(movie)
                    .category(categoryRepository.getCategoryByCategoryName(s))
                    .build();

            movieCategoryList.add(movieCategory);
            log.info("category = {}", s);
        }

        movie = movieRepository.getMovieByMovieName("클라우스");
        categoryString = "애니메이션, 코미디, 가족".replaceAll(" ", "");
        categoryList = categoryString.split(",");
        for (String s : categoryList) {
            MovieCategory movieCategory = MovieCategory
                    .builder()
                    .movie(movie)
                    .category(categoryRepository.getCategoryByCategoryName(s))
                    .build();

            movieCategoryList.add(movieCategory);
            log.info("category = {}", s);
        }

        movie = movieRepository.getMovieByMovieName("덩케르크");
        categoryString = "액션, 전쟁, 드라마".replaceAll(" ", "");
        categoryList = categoryString.split(",");
        for (String s : categoryList) {
            MovieCategory movieCategory = MovieCategory
                    .builder()
                    .movie(movie)
                    .category(categoryRepository.getCategoryByCategoryName(s))
                    .build();

            movieCategoryList.add(movieCategory);
            log.info("category = {}", s);
        }

        movieCategoryRepository.saveAll(movieCategoryList);
    }

    @Test
    void createBrandMovies() {
        List<Movie> movieList = movieRepository.findAll();

        List<Brand> brandList = brandRepository.findAll();
        List<BrandMovie> brandMovieList = new ArrayList<>();

        for (Movie movie : movieList) {
            for (Brand brand : brandList) {
                BrandMovie brandMovie = BrandMovie
                        .builder()
                        .movie(movie)
                        .brand(brand)
                        .build();

                brandMovieList.add(brandMovie);
            }
        }

        brandMovieRepository.saveAll(brandMovieList);
    }
}
