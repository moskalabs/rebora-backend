package moska.rebora.Admin.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.*;
import moska.rebora.Cinema.Dto.CinemaMovieDto;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Entity.MovieCinema;
import moska.rebora.Cinema.Repository.BrandRepository;
import moska.rebora.Cinema.Repository.CinemaRepository;
import moska.rebora.Cinema.Repository.MovieCinemaRepository;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Common.Repository.CategoryRepository;
import moska.rebora.Common.Service.FileUploadService;
import moska.rebora.Common.Util;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationManager;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Entity.MovieCategory;
import moska.rebora.Movie.Repository.MovieCategoryRepository;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Dto.TheaterPageDto;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final MovieCinemaRepository movieCinemaRepository;

    UserRepository userRepository;

    MovieRepository movieRepository;

    CategoryRepository categoryRepository;

    PasswordAuthAuthenticationManager authenticationManager;

    JwtAuthTokenProvider jwtAuthTokenProvider;

    PasswordEncoder passwordEncoder;

    FileUploadService fileUploadService;

    MovieCategoryRepository movieCategoryRepository;

    RecruitmentRepository recruitmentRepository;

    TheaterRepository theaterRepository;

    BrandRepository brandRepository;

    PaymentRepository paymentRepository;

    CinemaRepository cinemaRepository;

    Util util;

    @Override
    public UserLoginDto adminLogin(String userEmail, String password) {
        User user = userRepository.getUserByUserEmail(userEmail);
        if (user == null) {
            throw new NullPointerException("아이디가 일치하지 않습니다. \n입력하신 내용을 다시 확인해 주세요.");
        }

        if (!user.getUserUseYn()) {
            throw new NullPointerException("탈퇴된 회원입니다.");
        }

        if (!user.getUserGrade().equals(UserGrade.ADMIN)) {
            throw new NullPointerException("관리자등급이 아닙니다.");
        }

        return UserLoginDto.builder().token(createToken(userEmail, password)).result(true).errorCode(null).user(user).build();
    }

    @Override
    public BasePageResponse<MoviePageDto> getAdminList(UserSearchCondition searchCondition, Pageable pageable) {
        BasePageResponse<MoviePageDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(movieRepository.getAdminMovieList(searchCondition, pageable));

        return basePageResponse;
    }

    @Override
    @Transactional
    public void changeMoviePopularCount(String movieAll, Long movieId) {
        if (movieAll.equals("all")) {
            movieRepository.updateMoviePopularCount();
        } else {
            Optional<Movie> movieOptional = movieRepository.findById(movieId);
            if (movieOptional.isEmpty()) {
                throw new NullPointerException("존재하지 않는 영화입니다.");
            }

            Movie movie = movieOptional.get();
            movie.changeMoviePopularCount(movie.getMoviePopularCount());

            movieRepository.save(movie);
        }
    }

    @Override
    public void adminCheck(String userEmail) {
        User user = userRepository.getUserByUserEmail(userEmail);
        if (user == null) {
            throw new NullPointerException("존재하지 않는 아이디 입니다.");
        }

        if (!user.getUserGrade().equals(UserGrade.ADMIN)) {
            throw new JwtException("관리자만 접근 가능합니다.");
        }
    }

    @Override
    public BaseInfoResponse<AdminMovieDto> getMovieInfo(Long movieId) {
        BaseInfoResponse<AdminMovieDto> baseInfoResponse = new BaseInfoResponse<>();

        if (movieId == null) {
            AdminMovieDto adminMovieDto = new AdminMovieDto();
            List<AdminMovieCategoryDto> movieCategoryDtoList = new ArrayList<>();

            List<Category> categoryList = categoryRepository.findAll();
            List<Cinema> cinemaList = cinemaRepository.findAll();

            movieCategoryDtoList = categoryList.stream().map(AdminMovieCategoryDto::new).collect(Collectors.toList());
            List<CinemaMovieDto> cinemaMovieDtoList = cinemaList.stream().map(CinemaMovieDto::new).collect(Collectors.toList());

            adminMovieDto.setCategoryList(movieCategoryDtoList);
            adminMovieDto.setCinemaMovieDtoList(cinemaMovieDtoList);
            baseInfoResponse.setResult(true);
            baseInfoResponse.setContent(adminMovieDto);

        } else {

            MoviePageDto moviePageDto = movieRepository.getMovieInfo(movieId);
            AdminMovieDto adminMovieDto = new AdminMovieDto(moviePageDto);

            List<AdminMovieCategoryDto> movieCategoryDtoList = new ArrayList<>();

            List<Category> categoryList = categoryRepository.findAll();
            List<Cinema> cinemaList = cinemaRepository.findAll();

            movieCategoryDtoList = categoryList.stream().map(AdminMovieCategoryDto::new).collect(Collectors.toList());
            List<CinemaMovieDto> cinemaMovieDtoList = cinemaList.stream().map(CinemaMovieDto::new).collect(Collectors.toList());

            List<Category> moviePageCategoryList = moviePageDto.getCategoryList();
            List<Cinema> cinemaSearchList = cinemaRepository.getCinemaListByMovieId(movieId);

            List<AdminMovieCategoryDto> finalMovieCategoryDtoList = movieCategoryDtoList;
            moviePageCategoryList.forEach(c -> {
                finalMovieCategoryDtoList.forEach(mc -> {
                    if (mc.getCategoryName().equals(c.getCategoryName())) {
                        mc.setCategoryYn(true);
                    }
                });
            });

            cinemaSearchList.forEach(c -> {
                String cinemaName = c.getBrandName() + " " + c.getRegionName() + " " + c.getCinemaName();
                cinemaMovieDtoList.forEach(cinema -> {
                    String compareName = cinema.getBrandName() + " " + cinema.getRegionName() + " " + cinema.getCinemaName();
                    if (cinemaName.equals(compareName)) {
                        cinema.setCinemaYn(true);
                    }
                });
            });

            adminMovieDto.setCategoryList(finalMovieCategoryDtoList);
            adminMovieDto.setCinemaMovieDtoList(cinemaMovieDtoList);
            log.info("cinemaList={}", cinemaMovieDtoList);
            baseInfoResponse.setResult(true);
            baseInfoResponse.setContent(adminMovieDto);
        }

        return baseInfoResponse;
    }

    @Override
    public RecruitmentInfoDto getRecruitmentInfo(Long recruitmentId) {
        return recruitmentRepository.getAdminRecruitmentInfo(recruitmentId);
    }

    @Override
    public void changeRecruitment(Long recruitmentId, RecruitmentStatus recruitmentStatus, Boolean recruitmentExposeYn) {
        Optional<Recruitment> recruitmentOptional = recruitmentRepository.findById(recruitmentId);

        if (recruitmentOptional.isEmpty()) {
            throw new NullPointerException("해당 모집이 없습니다.");
        }

        Recruitment recruitment = recruitmentOptional.get();
        recruitment.updateRecruitmentStatus(recruitmentStatus);
        recruitment.changeExpose(recruitmentExposeYn);

        recruitmentRepository.save(recruitment);
    }

    @Override
    public void changeMovie(
            Long movieId,
            String movieName,
            String movieRating,
            String movieDirector,
            String movieStarRating,
            String category,
            String cinema,
            String movieDetailLink,
            Integer movieRunningTime,
            Integer moviePopularCount,
            MultipartFile changeMovieImage,
            MultipartFile changeMovieBannerImage,
            MultipartFile changeMovieRecruitmentImage) {

        if (movieId == null) {
            createMovie(movieName, movieRating, movieDirector, movieStarRating, category, cinema, movieDetailLink, movieRunningTime, moviePopularCount, changeMovieImage, changeMovieBannerImage, changeMovieRecruitmentImage);
        } else {
            updateMovie(movieId, movieName, movieRating, movieDirector, movieStarRating, category, cinema, movieDetailLink, movieRunningTime, moviePopularCount, changeMovieImage, changeMovieBannerImage, changeMovieRecruitmentImage);
        }
    }

    @Transactional
    void createMovie(String movieName, String movieRating, String movieDirector, String movieStarRating, String category, String cinema, String movieDetailLink, Integer movieRunningTime, Integer moviePopularCount, MultipartFile changeMovieImage, MultipartFile changeMovieBannerImage, MultipartFile changeMovieRecruitmentImage) {

        int convertMovieStarRating = 0;
        String[] startRatingList = movieStarRating.split("\\.");
        for (int i = 0; i < startRatingList.length; i++) {
            if (i == 0) {
                ;
                convertMovieStarRating += (Integer.parseInt(startRatingList[i]) * 10);
            } else {
                convertMovieStarRating += Integer.parseInt(startRatingList[i]);
            }
        }

        Movie movie = Movie.builder().movieName(movieName).movieRating(MovieRating.valueOf(movieRating)).movieDirector(movieDirector).movieDetailLink(movieDetailLink).movieRunningTime(movieRunningTime).movieStarRating(convertMovieStarRating).moviePopularCount(moviePopularCount).build();

        movieRepository.save(movie);

        if (changeMovieImage != null) {
            movie.changeMovieImage(uploadMovieImage(changeMovieImage, movie.getId(), "poster"));
        }

        if (changeMovieBannerImage != null) {
            movie.changeMovieBannerImage(uploadMovieImage(changeMovieBannerImage, movie.getId(), "banner"));
        }

        if (changeMovieRecruitmentImage != null) {
            movie.changeMovieRecruitmentImage(uploadMovieImage(changeMovieRecruitmentImage, movie.getId(), "info"));
        }

        List<MovieCinema> movieCinemaList = new ArrayList<>();
        List<MovieCategory> movieCategoryList = new ArrayList<>();

        if (cinema != null) {
            String[] cinemaList = cinema.split(",");
            for (String s : cinemaList) {
                Cinema cinema1 = cinemaRepository.getReferenceById(Long.valueOf(s));
                MovieCinema movieCinema = MovieCinema.builder().cinema(cinema1).movie(movie).build();

                movieCinemaList.add(movieCinema);
            }
            movieCinemaRepository.saveAll(movieCinemaList);
        }

        if (category != null) {
            String[] categoryList = category.split(",");
            for (String s : categoryList) {
                Category category1 = categoryRepository.getReferenceById(Long.valueOf(s));
                MovieCategory movieCategory = MovieCategory.builder().movie(movie).category(category1).build();

                movieCategoryList.add(movieCategory);
            }

            movieCategoryRepository.saveAll(movieCategoryList);
        }
    }

    @Transactional
    void updateMovie(Long movieId, String movieName, String movieRating, String movieDirector, String movieStarRating, String category, String cinema, String movieDetailLink, Integer movieRunningTime, Integer moviePopularCount, MultipartFile changeMovieImage, MultipartFile changeMovieBannerImage, MultipartFile changeMovieRecruitmentImage) {
        Movie movie = movieRepository.getMovieById(movieId);
        int convertMovieStarRating = 0;
        String[] startRatingList = movieStarRating.split("\\.");
        for (int i = 0; i < startRatingList.length; i++) {
            if (i == 0) {
                ;
                convertMovieStarRating += (Integer.parseInt(startRatingList[i]) * 10);
            } else {
                convertMovieStarRating += Integer.parseInt(startRatingList[i]);
            }
        }

        if (changeMovieImage != null) {
            movie.changeMovieImage(uploadMovieImage(changeMovieImage, movieId, "poster"));
        }

        if (changeMovieBannerImage != null) {
            movie.changeMovieBannerImage(uploadMovieImage(changeMovieBannerImage, movieId, "banner"));
        }

        if (changeMovieRecruitmentImage != null) {
            movie.changeMovieRecruitmentImage(uploadMovieImage(changeMovieRecruitmentImage, movieId, "info"));
        }

        movie.updateMovie(movieName, movieRating, movieDirector, convertMovieStarRating, movieDetailLink, moviePopularCount, movieRunningTime);
        movieRepository.save(movie);


        List<MovieCategory> movieCategories = movieCategoryRepository.getMovieCategoryByMovie(movie);
        if (movieCategories.size() != 0) {
            movieCategoryRepository.deleteAll(movieCategories);
        }

        List<MovieCinema> movieCinemas = movieCinemaRepository.getMovieCinemasByMovieId(movieId);
        if (movieCinemas.size() != 0) {
            movieCinemaRepository.deleteAll(movieCinemas);
        }

        List<MovieCinema> movieCinemaList = new ArrayList<>();
        List<MovieCategory> movieCategoryList = new ArrayList<>();

        if (cinema != null) {
            String[] cinemaList = cinema.split(",");
            for (String s : cinemaList) {
                Cinema cinema1 = cinemaRepository.getReferenceById(Long.valueOf(s));
                MovieCinema movieCinema = MovieCinema.builder().cinema(cinema1).movie(movie).build();

                movieCinemaList.add(movieCinema);
            }
            movieCinemaRepository.saveAll(movieCinemaList);
        }

        if (category != null) {
            String[] categoryList = category.split(",");
            for (String s : categoryList) {
                Category category1 = categoryRepository.getReferenceById(Long.valueOf(s));
                MovieCategory movieCategory = MovieCategory.builder().movie(movie).category(category1).build();

                movieCategoryList.add(movieCategory);
            }

            movieCategoryRepository.saveAll(movieCategoryList);
        }
    }

    @Override
    public Page<RecruitmentInfoDto> getRecruitmentPage(Pageable pageable, UserSearchCondition userSearchCondition) {
        return recruitmentRepository.getRecruitmentPage(pageable, userSearchCondition);
    }

    String uploadMovieImage(MultipartFile file, Long movieId, String fileName) {

        String originalFileName = file.getOriginalFilename();
        String ext = FilenameUtils.getExtension(originalFileName);
        String newFileName = "movie/" + movieId + "/" + fileName + "." + ext;
        return fileUploadService.uploadImage(file, newFileName); //파일 Url
    }

    @Override
    public AdminTheaterResponseDto getAdminTheaterPage(String theaterRegion, String theaterCinemaBrandName, LocalDate selectDate, Pageable pageable) {
        AdminTheaterResponseDto adminTheaterResponseDto = new AdminTheaterResponseDto();
        adminTheaterResponseDto.setResult(true);
        adminTheaterResponseDto.setPage(theaterRepository.getAdminPage(theaterRegion, theaterCinemaBrandName, selectDate, pageable));
        adminTheaterResponseDto.setAdminRegionList(theaterRepository.getAdminRegion());
        return adminTheaterResponseDto;
    }

    @Override
    public AdminPaymentDto getPaymentInfo(String paymentId) {
        return paymentRepository.getPaymentInfo(paymentId);
    }

    @Override
    public void createTheaters(MultipartFile file) {

        log.info(file.getOriginalFilename() + " CSV 업로드 시작");
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "EUC-KR"));

            CSVReader csvReader = new CSVReader(reader);
            csvReader.readNext();
            readTheaterFile(csvReader);
            log.info(file.getOriginalFilename() + " CSV 업로드 종료");
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Theater> readTheaterFile(CSVReader csvReader) {

        List<Theater> theaterList = new ArrayList<>();
        try {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                if (line[0].equals("")) {
                    continue;
                }

                LocalDateTime theaterStartTime = convertDateTime(line[4], line[5]);
                Optional<Cinema> optionalCinema = cinemaRepository.getCinemaByCinemaNameAndRegionName(line[1], line[0]);

                if (optionalCinema.isEmpty()) {
                    throw new NullPointerException("존재하지 않는 극장입니다. 극장을 먼저 생성해주세요");
                }

                log.info(line[7]);
                Theater theater = Theater.builder().theaterRegion(line[0]).theaterCinemaName(line[1]).theaterName(line[2]).theaterCinemaBrandName(line[3]).brand(brandRepository.getBrandByBrandName(line[3])).theaterStartDatetime(theaterStartTime).theaterEndDatetime(convertDateTime(line[4], line[6])).theaterDay(theaterStartTime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA)).theaterTime(Integer.parseInt(line[7])).theaterMinPeople(Integer.parseInt(line[8])).theaterMaxPeople(Integer.parseInt(line[9])).theaterPrice(Integer.parseInt(line[10])).cinema(optionalCinema.get()).build();

                theaterList.add(theater);
            }

            theaterRepository.saveAll(theaterList);
        } catch (DateTimeParseException | NumberFormatException e) {
            log.error(e.getMessage());
            throw new RuntimeException("옳바르지 않은 날짜입니다. 다시 시도해 주세요");
        } catch (CsvValidationException e) {
            throw new RuntimeException("CSV 가져오던 도중 오류가 발생했습니다. 다시 시도해 주세요");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return theaterList;
    }

    public LocalDateTime convertDateTime(String date, String time) {
        String[] dateList = date.split("\\.");
        String[] timeList = time.split(":");
        log.info(Arrays.toString(timeList));
        log.info("time1={}", Integer.parseInt(timeList[0]));
        log.info("time2={}", Integer.parseInt(timeList[1]));
        return LocalDateTime.of(Integer.parseInt(dateList[0]), Integer.parseInt(dateList[1]), Integer.parseInt(dateList[2]), Integer.parseInt(timeList[0]), Integer.parseInt(timeList[1]));
    }

    @Override
    public AdminTheaterDto getAdminTheaterDetail(Long theaterId) {
        return theaterRepository.getAdminDetail(theaterId);
    }

    @Override
    public void theaterSave(Long theaterId, String theaterCinemaBrandName, String theaterRegion, String theaterCinemaName, String theaterName, String theaterDate, String theaterStartHour, String theaterStartMinute, String theaterEndHour, String theaterEndMinute, Integer theaterTime, Integer theaterPrice, Integer theaterMinPeople, Integer theaterMaxPeople) {

        LocalDateTime theaterStartDatetime = convertTheaterDateTime(theaterDate, theaterStartHour, theaterStartMinute);
        LocalDateTime theaterEndDatetime = convertTheaterDateTime(theaterDate, theaterEndHour, theaterEndMinute);

        if (theaterId != null) {

            Optional<Theater> theaterOptional = theaterRepository.findById(theaterId);

            if (theaterOptional.isEmpty()) {
                throw new NullPointerException("존재하지 않는 상영관 정보입니다.");
            }

            Optional<Cinema> optionalCinema = cinemaRepository.getCinemaByCinemaNameAndRegionName(theaterCinemaName, theaterRegion);

            if (optionalCinema.isEmpty()) {
                throw new NullPointerException("존재하지 않는 극장입니다. 먼저 극장을 생성해주세요");
            }

            Theater theater = theaterOptional.get();
            theater.updateTheater(theaterName, theaterStartDatetime, theaterEndDatetime, theaterStartDatetime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA), theaterMaxPeople, theaterMinPeople, theaterCinemaName, theaterCinemaBrandName, theaterRegion, theaterPrice, theaterTime, brandRepository.getBrandByBrandName(theaterCinemaBrandName));

            theaterRepository.save(theater);

        } else {

            Optional<Cinema> optionalCinema = cinemaRepository.getCinemaByCinemaNameAndRegionName(theaterCinemaName, theaterRegion);

            if (optionalCinema.isEmpty()) {
                throw new NullPointerException("존재하지 않는 극장입니다. 먼저 극장을 생성해주세요");
            }

            Theater theater = Theater.builder().theaterName(theaterName).theaterStartDatetime(theaterStartDatetime).theaterEndDatetime(theaterEndDatetime).theaterDay(theaterStartDatetime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA)).theaterMaxPeople(theaterMaxPeople).theaterMinPeople(theaterMinPeople).theaterCinemaName(theaterCinemaName).theaterCinemaBrandName(theaterCinemaBrandName).theaterRegion(theaterRegion).theaterPrice(theaterPrice).theaterTime(theaterTime).brand(brandRepository.getBrandByBrandName(theaterCinemaBrandName)).cinema(optionalCinema.get()).build();

            theaterRepository.save(theater);
        }
    }

    @Override
    public Page<AdminUserDto> getUserPage(UserSearchCondition userSearchCondition, String startDate, String endDate, UserGrade userGrade, String userSnsKind, Pageable pageable) {
        return userRepository.getAdminUserPage(userSearchCondition, startDate, endDate, userGrade, userSnsKind, pageable);
    }

    @Override
    public void saveUser(Long userId, String userEmail, String userName, MultipartFile userImage, Boolean userPushYn, Boolean userPushNightYn, Boolean userUseYn, String userGrade, String userPassword, String userNickname) {

        if (userId != null) {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                throw new NullPointerException("해당하는 유저가 없습니다.");
            }

            User user = userOptional.get();
            String fileUrl = user.getUserImage();

            if (userImage != null) {
                String originalFileName = userImage.getOriginalFilename(); //원본 파일 이름
                String ext = FilenameUtils.getExtension(originalFileName); //확장자
                String newFileName = "user/" + userId + "/" + userId + "_" + util.createRandomString(8) + "." + ext; //새로운 파일 이름
                fileUrl = fileUploadService.uploadImage(userImage, newFileName); //파일 Url
            }

            if (fileUrl == null) {
                fileUrl = "";
            }

            user.changeAdminUserInfo(userEmail, userName, fileUrl, userPushYn, userPushNightYn, userUseYn, userGrade);

            if (!userPassword.equals("")) {
                String bcryptPassword = passwordEncoder.encode(userPassword);
                user.changePassword(bcryptPassword);
            }

            userRepository.save(user);

        } else {

            String bcryptPassword = passwordEncoder.encode(userPassword);

            User user = User
                    .builder()
                    .userEmail(userEmail)
                    .password(bcryptPassword)
                    .userSnsId("")
                    .userSnsKind(null)
                    .userImage("")
                    .userName(userName)
                    .userNickname(userNickname)
                    .userGrade(UserGrade.valueOf(userGrade))
                    .userUseYn(userUseYn)
                    .userPushYn(userPushYn)
                    .userPushNightYn(userPushNightYn)
                    .build();

            userRepository.save(user);

            String fileUrl = "";

            if (userImage != null) {
                String originalFileName = userImage.getOriginalFilename(); //원본 파일 이름
                String ext = FilenameUtils.getExtension(originalFileName); //확장자
                String newFileName = "user/" + userId + "/" + userId + "_" + util.createRandomString(8) + "." + ext; //새로운 파일 이름
                fileUrl = fileUploadService.uploadImage(userImage, newFileName); //파일 Url
            }

            user.updateUserImage(fileUrl);

            userRepository.save(user);
        }
    }

    @Override
    public Page<AdminPaymentDto> getPaymentPage(UserSearchCondition userSearchCondition, String startDate, String endDate, Pageable pageable) {
        LocalDate today = LocalDate.now();
        LocalDate toDate;
        LocalDate fromDate;

        if (startDate.equals("")) {
            fromDate = today.minusDays(30);
        } else {
            fromDate = LocalDate.parse(startDate);
        }

        if (endDate.equals("")) {
            toDate = today;
        } else {
            toDate = LocalDate.parse(endDate);
        }

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);

        return paymentRepository.getPaymentDto(pageable, userSearchCondition, fromDateTime, toDateTime);
    }

    public LocalDateTime convertTheaterDateTime(String theaterDate, String Hour, String Minute) {
        String[] dateList = theaterDate.split("-");
        int Year = Integer.parseInt(dateList[0]);
        int Month = Integer.parseInt(dateList[1]);
        int Date = Integer.parseInt(dateList[2]);
        return LocalDateTime.of(Year, Month, Date, Integer.parseInt(Hour), Integer.parseInt(Minute));
    }

    /**
     * 토큰 발행
     *
     * @param userEmail 유저이메일
     * @param password  비밀번호
     * @return String
     */
    public String createToken(String userEmail, String password) {

        Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

        PasswordAuthAuthenticationToken token = new PasswordAuthAuthenticationToken(userEmail, password);
        Authentication authentication = authenticationManager.authenticate(token);
        PasswordAuthAuthenticationToken authToken = (PasswordAuthAuthenticationToken) authentication;
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> claims = new HashMap<>();
        claims.put("id", authToken.getId().toString());
        claims.put("userName", authToken.getUserName());
        claims.put("role", "ADMIN");
        claims.put("userEmail", authToken.getUserEmail());

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(authentication.getName(), "ADMIN", claims, expiredDate);

        return jwtAuthToken.getToken(jwtAuthToken);
    }

    @Override
    public void uploadMovieCsvFile(MultipartFile file) {
        log.info(file.getOriginalFilename() + " CSV 업로드 시작");
        Reader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

            CSVReader csvReader = new CSVReader(reader);
            csvReader.readNext();
            readMovieFile(csvReader);
            log.info(file.getOriginalFilename() + " CSV 업로드 종료");
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void readMovieFile(CSVReader csvReader) {
        try {
            String[] line;

            List<String> movieNameList = new ArrayList<>();

            while ((line = csvReader.readNext()) != null) {
                if (line[0].equals("")) {
                    continue;
                }
                movieNameList.add(line[0]);

                log.info("영화 이름={}", line[0]);
                log.info("영화 감독={}", line[1]);
                log.info("영화 등급={}", line[2]);
                log.info("영화 평점={}", line[3]);
                log.info("영화 상세 링크={}", line[4]);
                log.info("영화 러닝 타임={}", line[5]);
                log.info("카테고리={}", line[6]);

                onValidMovie(line[0], line[3], line[5]);


                Movie movie = Movie.builder()
                        .movieName(line[0])
                        .movieDirector(line[1])
                        .movieRating(MovieRating.valueOf(line[2]))
                        .movieStarRating(Integer.parseInt(line[3]))
                        .movieDetailLink(line[4])
                        .movieRunningTime(Integer.parseInt(line[5]))
                        .movieUseYn(false)
                        .moviePopularCount(0)
                        .build();

                movieRepository.save(movie);

                if (line[6] != null) {
                    List<Category> categoryList = new ArrayList<>();
                    List<MovieCategory> movieCategoryList = new ArrayList<>();
                    categoryList = categoryRepository.getCategoriesByCategoryNameIn(line[6].split(","));
                    for (Category category : categoryList) {
                        MovieCategory movieCategory = MovieCategory.builder().movie(movie).category(category).build();
                        movieCategoryList.add(movieCategory);
                    }
                    movieCategoryRepository.saveAll(movieCategoryList);
                }
            }

            Integer movieDuplicateCount = movieRepository.countMovieByMovieNameIn(movieNameList);
            log.info("영화 중복 개수={}", movieDuplicateCount);

            if (movieDuplicateCount > 0) {
                throw new RuntimeException("중복된 영화 이름이 있습니다. 다시 시도해주세요.");
            }

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException("CSV 가져오던 도중 오류가 발생했습니다. \n 다시 시도해 주세요");
        }
    }

    public void onValidMovie(String movieName, String movieStarRating, String movieRunningTime) {

        try {
            Integer.parseInt(movieStarRating);
            Integer.parseInt(movieRunningTime);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(movieName + "의 정보가 잘못 되었습니다.");
        }
    }

    @Override
    @Transactional
    public void uploadMovieImageFile(List<MultipartFile> fileList) {

        if (fileList.isEmpty()) {
            throw new NullPointerException("파일이 존재하지 않습니다. 다시 시도해주세요.");
        }

        List<String> movieNameList = new ArrayList<>();

        fileList.forEach(file -> {
            String[] splitExt = file.getOriginalFilename().split("\\.");
            log.info("ext={}", splitExt[splitExt.length - 1]);

            String remainderString = splitExt[splitExt.length - 2];
            String[] splitFilePath = remainderString.split("/");

            String originFileName = splitFilePath[splitFilePath.length - 1];
            String[] splitMovieName = originFileName.split("_");

            log.info("movieName={}", splitMovieName[0]);
            log.info("imageKind={}", splitMovieName[1]);

            if (!splitMovieName[1].equals("poster") && !splitMovieName[1].equals("info") && !splitMovieName[1].equals("banner")) {
                throw new NullPointerException("옳바른 형식의 파일이 아닙니다. " + "<br> 영화 이름 : " + originFileName + "." + splitExt[splitExt.length - 1]);
            }

            String replaceName = splitMovieName[0].replaceAll("\\^", ":");

            if (!movieNameList.contains(replaceName)) {
                movieNameList.add(replaceName);
            }
        });

        Integer movieCount = movieRepository.countMovieByMovieNameIn(movieNameList);
        log.info("movieNameListSize={}", movieNameList.size());
        log.info("movieCount={}", movieCount);

        if (movieCount != movieNameList.size()) {
            throw new NullPointerException("존재 하지 않는 영화 제목이 있습니다.");
        }

        List<Movie> movieList = movieRepository.getMoviesByMovieNameIn(movieNameList);
        String path = "https://rebora.s3.ap-northeast-2.amazonaws.com/movie/";
        movieList.forEach(movie -> {
            String movieImage = path + movie.getId() + "/poster.png";
            String movieBannerImage = path + movie.getId() + "/banner.png";
            String movieRecruitmentImage = path + movie.getId() + "/info.png";

            movie.addImage(movieImage, movieBannerImage, movieRecruitmentImage);
        });

        movieRepository.saveAll(movieList);

        fileList.forEach(file -> {
            String[] splitExt = file.getOriginalFilename().split("\\.");
            log.info("ext={}", splitExt[splitExt.length - 1]);

            String remainderString = splitExt[splitExt.length - 2];
            String[] splitFilePath = remainderString.split("/");

            String originFileName = splitFilePath[splitFilePath.length - 1];
            String[] splitMovieName = originFileName.split("_");
            String replaceName = splitMovieName[0].replaceAll("\\^", ":");
            log.info("movieName={}", replaceName);
            log.info("imageKind={}", splitMovieName[1]);

            Movie movie = movieRepository.getMovieByMovieName(replaceName);

            String newFileName = "movie/" + movie.getId() + "/" + splitMovieName[1] + "." + splitExt[splitExt.length - 1];
            fileUploadService.uploadImage(file, newFileName); //파일 Url
        });
    }

    @Override
    public void updatePaymentInfo(String paymentId, String paymentMemo) {
        Optional<Payment> paymentOptional = paymentRepository.getPaymentById(paymentId);

        if (paymentOptional.isEmpty()) {
            throw new NullPointerException("결제 번호가 존재하지 않습니다.");
        }

        Payment payment = paymentOptional.get();
        payment.updateMemo(paymentMemo);

        paymentRepository.save(payment);
    }
}