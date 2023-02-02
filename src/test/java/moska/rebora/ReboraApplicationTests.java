package moska.rebora;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.opencsv.CSVReader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.AdminRegionDto;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Cinema.Entity.BrandMovie;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Entity.MovieCinema;
import moska.rebora.Cinema.Repository.BrandMovieRepository;
import moska.rebora.Cinema.Repository.BrandRepository;
import moska.rebora.Cinema.Repository.CinemaRepository;
import moska.rebora.Cinema.Repository.MovieCinemaRepository;
import moska.rebora.Common.Dto.FCMMessage;
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
import moska.rebora.User.DTO.ApplePublicKeyDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.Policy;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.PolicyRepository;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import moska.rebora.User.Service.OathService;
import org.apiguardian.api.API;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static moska.rebora.Common.CommonConst.NAVER_TOKEN_ME_URL;


@Slf4j
@SpringBootTest
class ReboraApplicationTests {
    @Autowired
    private MovieCinemaRepository movieCinemaRepository;

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
    @Autowired
    CinemaRepository cinemaRepository;

    @Autowired
    OathService oathService;

    @Autowired
    private RestTemplate restTemplate;

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

//    @Test
//    @Transactional
//    void testPay() throws IOException, InterruptedException {
//
//        User user = userTestRepository.getUserById(1L);
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, String> params = new HashMap<>();
//        params.put("customerKey", user.getUserCustomerId());
//        params.put("amount", "1000");
//        params.put("orderId", "recruitment_1_2");
//        params.put("orderName", "모집_1_2명");
//        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
//        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://api.tosspayments.com/v1/billing/" + user.getUserBillingKey()))
//                .header("Authorization", "Basic dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==")
//                .header("Content-Type", "application/json")
//                .method("POST", body)
//                .build();
//
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        log.info(String.valueOf(response.statusCode()));
//        log.info(response.body());
//    }

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
    void testFailJon() {
        List<Payment> paymentList = paymentRepository.getBatchPaymentList(PaymentStatus.FAILURE);
    }

    @Test
    @Transactional
    @Rollback(false)
    void fileCateGoryUpload() {
        File file = new File("/Users/kibong/Downloads/movieCategory.csv");

        try {
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            CSVReader csvReader = new CSVReader(reader);
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                //log.info("line={}", line);
                if (line[0].equals("")) {
                    continue;
                }
                log.info("fileLin0={}", line[0]);
                log.info("fileLine1={}", line[1]);
                Movie movie = movieRepository.getMovieByMovieName(line[0]);
                log.info("movieName={}", movie.getMovieName());
                String[] categoryList = line[1].split(",");
                for (String s : categoryList) {
                    Category category = categoryRepository.getCategoryByCategoryName(s);
                    MovieCategory movieCategory = MovieCategory.builder().movie(movie).category(category).build();
                    movieCategoryRepository.save(movieCategory);
                    log.info("movieCategoryId={}", movieCategory.getId());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional
    void testFileUpload() {

        Path dirPath = Paths.get("/Users/kibong/Downloads/movie");

        System.out.println(dirPath);
        List<Path> result;
        Stream<Path> walk = null;

        try {
            walk = Files.walk(dirPath).filter(Files::isDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        result = walk.collect(Collectors.toList());
        for (int i = 1; i < result.size(); i++) {
            log.info("file={}", result.get(i));
            Path path = result.get(i);
            String pathString = path.toString();
            String dir = pathString.split("/")[5];
            File file = new File(pathString);
            Movie movie = movieRepository.getMovieByMovieName(dir);
            log.info("movieId={}", movie.getId());
            String reNamepath = "/Users/kibong/Downloads/movie/" + movie.getId();
            File file2 = new File(reNamepath);
            log.info("fileName={}", file.renameTo(file2));
        }

//        for (int i = 1; i < result.size(); i++) {
//
//            Path path = result.get(i);
//            String pathString = path.toString();
//            File file = new File(pathString);
//            String fileName = file.getName();
//            String[] fileNameList = fileName.split(" ");
//            String movieName = "";
//            for (int j = 1; j < fileNameList.length; j++) {
//                if(j == 1){
//                    movieName += fileNameList[j];
//                }else{
//                    movieName += " "+fileNameList[j];
//                }
//            }
//            String reNamepath = "/Users/kibong/Downloads/movie/"+movieName;
//            File file2 = new File(reNamepath);
//
//            log.info("fileName={}", file.renameTo(file2));
//        }
    }

    @Test
    void findById() {

        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(1L);
        Recruitment recruitment = optionalRecruitment.get();
        Theater theater = recruitment.getTheater();
        log.info("theaterName={}", theater.getTheaterName());
    }

    @Test
    void createMovieBrand() {
        List<Movie> movieList = movieRepository.findAll();
        Brand brand = brandRepository.getBrandByBrandName("CGV");
        List<BrandMovie> brandMovieList = new ArrayList<>();
        movieList.forEach(movie -> {
            BrandMovie brandMovie = BrandMovie.builder()
                    .movie(movie)
                    .brand(brand)
                    .build();

            brandMovieList.add(brandMovie);
        });

        brandMovieRepository.saveAll(brandMovieList);
    }

    @Test
    void dateTest() {
        log.info("int test={}", Integer.parseInt("01"));
    }

    @Test
    void createCinema() {
        List<Movie> movieList = movieRepository.findAll();
        Cinema cinema = cinemaRepository.findById(1L).get();
        List<MovieCinema> movieCinemaList = new ArrayList<>();
        movieList.forEach(movie -> {
            MovieCinema movieCinema = MovieCinema.builder()
                    .movie(movie)
                    .cinema(cinema)
                    .build();

            movieCinemaList.add(movieCinema);
        });

        movieCinemaRepository.saveAll(movieCinemaList);
    }

    @Test
    @Transactional
    void cinemaTheaterTest() {
        List<Payment> paymentList = paymentRepository.getBatchPaymentList(PaymentStatus.FAILURE);
        log.info("payment Size={}", paymentList.size());
    }

    @Test
    void appleLoginTest() {
        List<ApplePublicKeyDto> applePublicKeyDtoList = new ArrayList<>();
        applePublicKeyDtoList = oathService.getPublicAppleKeys();

        for (ApplePublicKeyDto applePublicKeyDto : applePublicKeyDtoList) {
            log.info("applePublicKeyDto kid={}", applePublicKeyDto.getKid());
        }
    }

    @Test
    void naverLoginChange() {

    }

    @Test
    void createJwt() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        String compact = Jwts.builder()
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("kid", "W6WcOKB")
                .setIssuer("G87RV7262L")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience("https://appleid.apple.com")
                .compact();

        log.info("jwt={}", compact);
    }

//    @Test
//    void testJwts() throws IOException {
//        log.info("jwt={}", createJwt());
//    }

//    private Key getPrivateKey() throws IOException {
//        ClassPathResource resource = new ClassPathResource("static/AuthKey_8N747Q9974.p8");
//        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));
//        log.info("key={}", privateKey);
//        Reader pemReader = new StringReader(privateKey);
//        PEMParser pemParser = new PEMParser(pemReader);
//        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
//        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
//        return converter.getPrivateKey(object);
//    }

//    @Test
//    void privateKeyApple() throws IOException, ParseException, InterruptedException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> params = new HashMap<>();
//        params.put("client_id", "com.moca.rebora");
//        params.put("client_secret", createJwt());
//        params.put("code", "ced9ca8413f6e424ab4f5c2776528704b.0.srutz.Itg3IE1pNhNMcXizcwhltA");
//        params.put("grant_type", "authorization_code");
//        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
//        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create("https://appleid.apple.com/auth/token"))
//                .header("Content-Type", "application/x-www-form-urlencoded")
//                .method("POST", body)
//                .build();
//
//        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.body());
//
//        log.info("response={}", jsonObj);
//    }

    String firebase() throws IOException {
        String firebaseConfigPath = "static/rebora-98afa-firebase-adminsdk-4dgzs-196b51a679.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        String tokenValue = googleCredentials.getAccessToken().getTokenValue();
        log.info("token={}", tokenValue);

        return tokenValue;
    }

    @Test
    void messageFireBase() throws IOException, InterruptedException, ParseException {
        String API_URL = "https://fcm.googleapis.com/v1/projects/rebora-98afa/messages:send";
        ObjectMapper objectMapper = new ObjectMapper();

        FCMMessage fcmMessage = FCMMessage.builder()
                .message(
                        FCMMessage.Message
                                .builder()
                                .notification(FCMMessage.Notification.builder()
                                        .title("참여한 모집의 결제가 실패하였습니다.")
                                        .body("이미 주문이 이루어진 건입니다. (동일한 merchant_uid로 결제 또는 취소된 기록이 있습니다)")
                                        .image(null)
                                        .build())
                                .token("fMmBcLdJTmeVkqLsQSLgVC:APA91bGKIcfd3EBY7yepn2TyuyFzM1Y0WDrM4yLFq-yqgao2zupfp6JfZTR5M9RbqmzxMGWOgqhKdf--FY6rS1pFcQupsMtCAsp9r3PB2CCX0DQ4CVbC3ApgSC3zZGBxidwB2UpkDhdT")
                                .build()
                )
                .validate_only(false)
                .build();

        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fcmMessage);
        log.info("requestBody={}", requestBody);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", "Bearer " + firebase())
                .header("Content-Type", "application/json; UTF-8")
                .method("POST", body)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser jsonParser = new JSONParser();
        JSONObject result = (JSONObject) jsonParser.parse(response.body());
        log.info("result={}", result);
    }

    @Test
    @Transactional
    void getHour() throws IOException, ParseException, InterruptedException {
        User user = userTestRepository.getUserById(1L);
        LocalDateTime now = LocalDateTime.now();
        log.info("message start");
        if (now.getHour() > 22 || now.getHour() < 7) {
            if (user.getUserUseYn() && user.getUserPushYn() && user.getUserPushNightYn() && user.getUserPushKey() != null && !user.getUserPushKey().equals("")) {
                log.info("message afternoon start");
                messageFireBase();
            }
        } else {
            if (user.getUserUseYn() && user.getUserPushYn() && user.getUserPushKey() != null && !user.getUserPushKey().equals("")) {
                log.info("message night start");
                messageFireBase();
            }
        }
    }

    @Test
    @Transactional
    void paymentGet(){
        UserRecruitment userRecruitment = userRecruitmentRepository.findById(1L).get();
        Optional<Payment> optionalPayment = paymentRepository.getPaymentByUserRecruitment(userRecruitment);

        log.info("isEmpty={}" , optionalPayment.isEmpty());
    }
}
