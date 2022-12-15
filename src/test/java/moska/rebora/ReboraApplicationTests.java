package moska.rebora;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.AdminRegionDto;
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
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.Policy;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.PolicyRepository;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


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

    @Autowired
    PaymentService paymentService;

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
                .paymentContent("예약 결제")
                .paymentAmount(7000)
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

    @Test
    void seeKey() {
        Movie movie = movieRepository.getMovieById(1L);
        Category category = categoryRepository.getReferenceById(15L);
        MovieCategory movieCategory = MovieCategory
                .builder()
                .movie(movie)
                .category(category)
                .build();

        movieCategoryRepository.save(movieCategory);
    }

    @Test
    void seeTheaterRegion() {
        String date = "2022-12-08T10:29:43+09:00";
        String[] dateList = date.split("\\+");
        LocalDateTime dateTime = LocalDateTime.parse(dateList[0]);
        log.info("dateTime={}", dateTime);
    }

    @Test
    @Transactional
    void testPay() throws IOException, InterruptedException {

        User user = userTestRepository.getUserById(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = new HashMap<>();
        params.put("customerKey", user.getUserCustomerId());
        params.put("amount", "1000");
        params.put("orderId", "recruitment_1_2");
        params.put("orderName", "모집_1_2명");
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/billing/" + user.getUserBillingKey()))
                .header("Authorization", "Basic dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==")
                .header("Content-Type", "application/json")
                .method("POST", body)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        log.info(String.valueOf(response.statusCode()));
        log.info(response.body());
    }

    @Test
    @Transactional
    void testImport() throws IOException, InterruptedException, ParseException {

        String auth = getAuth();
        log.info("auto={}", auth);
        User user = userTestRepository.getUserById(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> schedules = new HashMap<>();
        schedules.put("schedule_at", "1670488500000");
        schedules.put("amount", "1000");
        schedules.put("merchant_uid", "recruitment_1_2");
        schedules.put("name", "테스트1");
        schedules.put("currency", "KRW");
        params.put("customer_uid", user.getUserBillingKey());
        params.put("schedules", schedules);
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
        log.info("requestBody={}", requestBody);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/subscribe/payments/schedule"))
                .header("Authorization", "Bearer " + auth)
                .header("Content-Type", "application/json")
                .method("POST", body)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        log.info(String.valueOf(response.statusCode()));
        log.info(response.body());
    }

    String getAuth() throws IOException, ParseException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = new HashMap<>();
        params.put("imp_key", "1552251027035377");
        params.put("imp_secret", "FBVSWCbJTmWqda0ZpZbPZTuhFhLOWyIvTnybuyAmQWACvdZeszaUY7Pp4uLgbNy1RTo3BviNFmAb54f4");
        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/users/getToken"))
                .header("Authorization", "Bearer dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==")
                .header("Content-Type", "application/json")
                .method("POST", body)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.body());
        JSONObject responseJson = (JSONObject) jsonObj.get("response");
        return String.valueOf(responseJson.get("access_token"));
    }

    @Test
    void testTimeStamp() {
        long inserted = 1670566562L;
        LocalDateTime authenticatedAt =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(inserted), TimeZone
                        .getDefault().toZoneId());
        log.info("authenticatedAt={}", authenticatedAt);
    }

    @Test
    void testRecruitment() {
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(6L);
        List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getUserRecruitmentByRecruitment(recruitment);
        log.info("size={}", userRecruitmentList.size());
    }

    @Test
    void testJob() {
        UserSearchCondition condition = new UserSearchCondition();
        condition.setRecruitmentStatus(RecruitmentStatus.CONFIRMATION);
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, condition);
        log.info("size={}", recruitmentList.size());
        for (Recruitment recruitment : recruitmentList) {
            log.info("id={}", recruitment.getId());
            paymentService.paymentByRecruitment(recruitment);
            recruitment.updateRecruitmentStatus(RecruitmentStatus.CONFIRMATION);
        }
        recruitmentRepository.saveAll(recruitmentList);
    }

    @Test
    void updateRecruitmentComplete() {
        log.info("now={}", LocalDateTime.now().minusDays(3).toLocalDate().atStartOfDay().plusSeconds(1L));
    }

    @Test
    @Transactional
    void testCancelJob() {
        UserSearchCondition condition = new UserSearchCondition();
        condition.setRecruitmentStatus(RecruitmentStatus.CANCEL);
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, condition);
        Recruitment recruitment = recruitmentList.get(0);
        List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getBatchRefundUserRecruitment(recruitment.getId());
        List<UserRecruitment> userWishRecruitmentList = userRecruitmentRepository.getBatchUserWishRecruitment(recruitment.getId());

        Movie movie = recruitment.getMovie();
        Theater theater = recruitment.getTheater();
        String notificationSubject = "참여한 모집이 취소되었습니다.";

        for (UserRecruitment userRecruitment : userRecruitmentList) {
            Payment payment = userRecruitment.getPayment();
            User user = userRecruitment.getUser();

            log.info("userRecruitmentId={}", userRecruitment.getId());
            log.info("paymentId={}", payment.getId());
            log.info("user={}", user.getId());

            String notificationContent = notificationService.createNotificationContent(
                    movie.getMovieName(),
                    theater.getTheaterStartDatetime(),
                    theater.getTheaterDay(),
                    theater.getTheaterCinemaBrandName(),
                    theater.getTheaterCinemaName(),
                    theater.getTheaterName()
            );

            log.info("notificationContent={}", notificationContent);

            Banner banner = bannerRepository.getBannerByRecruitment(recruitment);

            log.info("bannerId={}", banner.getId());
        }


    }

    @Test
    @Transactional
    void testFailJon(){
        List<Payment> paymentList = paymentRepository.getBatchPaymetList(PaymentStatus.FAILURE);
    }
}
