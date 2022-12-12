package moska.rebora.Admin.Service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.*;
import moska.rebora.Cinema.Repository.BrandRepository;
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

        return UserLoginDto.builder()
                .token(createToken(userEmail, password))
                .result(true)
                .errorCode(null)
                .user(user)
                .build();
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
            movieCategoryDtoList = categoryList.stream().map(AdminMovieCategoryDto::new).collect(Collectors.toList());

            adminMovieDto.setCategoryList(movieCategoryDtoList);
            baseInfoResponse.setResult(true);
            baseInfoResponse.setContent(adminMovieDto);
        } else {
            MoviePageDto moviePageDto = movieRepository.getMovieInfo(movieId);
            AdminMovieDto adminMovieDto = new AdminMovieDto(moviePageDto);

            List<AdminMovieCategoryDto> movieCategoryDtoList = new ArrayList<>();
            List<Category> categoryList = categoryRepository.findAll();
            movieCategoryDtoList = categoryList.stream().map(AdminMovieCategoryDto::new).collect(Collectors.toList());

            List<Category> moviePageCategoryList = moviePageDto.getCategoryList();

            List<AdminMovieCategoryDto> finalMovieCategoryDtoList = movieCategoryDtoList;
            moviePageCategoryList.forEach(c -> {
                finalMovieCategoryDtoList.forEach(mc -> {
                    if (mc.getCategoryName().equals(c.getCategoryName())) {
                        mc.setCategoryYn(true);
                    }
                });
            });

            adminMovieDto.setCategoryList(finalMovieCategoryDtoList);
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
            String movieDetailLink,
            Integer movieRunningTime,
            Integer moviePopularCount,
            MultipartFile changeMovieImage,
            MultipartFile changeMovieBannerImage,
            MultipartFile changeMovieRecruitmentImage) {

        if (movieId == null) {
            createMovie(
                    movieName,
                    movieRating,
                    movieDirector,
                    movieStarRating,
                    category,
                    movieDetailLink,
                    movieRunningTime,
                    moviePopularCount,
                    changeMovieImage,
                    changeMovieBannerImage,
                    changeMovieRecruitmentImage
            );
        } else {
            updateMovie(
                    movieId,
                    movieName,
                    movieRating,
                    movieDirector,
                    movieStarRating,
                    category,
                    movieDetailLink,
                    movieRunningTime,
                    moviePopularCount,
                    changeMovieImage,
                    changeMovieBannerImage,
                    changeMovieRecruitmentImage
            );
        }
    }

    @Transactional
    void createMovie(
            String movieName,
            String movieRating,
            String movieDirector,
            String movieStarRating,
            String category,
            String movieDetailLink,
            Integer movieRunningTime,
            Integer moviePopularCount,
            MultipartFile changeMovieImage,
            MultipartFile changeMovieBannerImage,
            MultipartFile changeMovieRecruitmentImage
    ) {
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

        Movie movie = Movie
                .builder()
                .movieName(movieName)
                .movieRating(MovieRating.valueOf(movieRating))
                .movieDirector(movieDirector)
                .movieDetailLink(movieDetailLink)
                .movieRunningTime(movieRunningTime)
                .movieStarRating(convertMovieStarRating)
                .moviePopularCount(moviePopularCount)
                .build();

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

        String[] categoryList = category.split(",");

        List<MovieCategory> movieCategoryList = new ArrayList<>();
        for (String s : categoryList) {
            Category category1 = categoryRepository.getReferenceById(Long.valueOf(s));
            MovieCategory movieCategory = MovieCategory
                    .builder()
                    .movie(movie)
                    .category(category1)
                    .build();

            movieCategoryList.add(movieCategory);
        }

        movieCategoryRepository.saveAll(movieCategoryList);
    }

    @Transactional
    void updateMovie(
            Long movieId,
            String movieName,
            String movieRating,
            String movieDirector,
            String movieStarRating,
            String category,
            String movieDetailLink,
            Integer movieRunningTime,
            Integer moviePopularCount,
            MultipartFile changeMovieImage,
            MultipartFile changeMovieBannerImage,
            MultipartFile changeMovieRecruitmentImage
    ) {
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

        String[] categoryList = category.split(",");

        List<MovieCategory> movieCategories = movieCategoryRepository.getMovieCategoryByMovie(movie);
        if (movieCategories.size() != 0) {
            movieCategoryRepository.deleteAll(movieCategories);
        }

        List<MovieCategory> movieCategoryList = new ArrayList<>();
        for (String s : categoryList) {
            Category category1 = categoryRepository.getReferenceById(Long.valueOf(s));
            MovieCategory movieCategory = MovieCategory
                    .builder()
                    .movie(movie)
                    .category(category1)
                    .build();

            movieCategoryList.add(movieCategory);
        }

        movieCategoryRepository.saveAll(movieCategoryList);
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
                log.info("line={}", line);
                if(line[0].equals("")){
                    continue;
                }
                LocalDateTime theaterStartTime = convertDateTime(line[4], line[5]);

                log.info(line[7]);
                Theater theater = Theater
                        .builder()
                        .theaterRegion(line[0])
                        .theaterCinemaName(line[1])
                        .theaterName(line[2])
                        .theaterCinemaBrandName(line[3])
                        .brand(brandRepository.getBrandByBrandName(line[3]))
                        .theaterStartDatetime(theaterStartTime)
                        .theaterEndDatetime(convertDateTime(line[4], line[6]))
                        .theaterDay(theaterStartTime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA))
                        .theaterTime(Integer.parseInt(line[7]))
                        .theaterMinPeople(Integer.parseInt(line[8]))
                        .theaterMaxPeople(Integer.parseInt(line[9]))
                        .theaterPrice(Integer.parseInt(line[10]))
                        .build();

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
    public void theaterSave(
            Long theaterId,
            String theaterCinemaBrandName,
            String theaterRegion,
            String theaterCinemaName,
            String theaterName,
            String theaterDate,
            String theaterStartHour,
            String theaterStartMinute,
            String theaterEndHour,
            String theaterEndMinute,
            Integer theaterTime,
            Integer theaterPrice,
            Integer theaterMinPeople,
            Integer theaterMaxPeople) {

        LocalDateTime theaterStartDatetime = convertTheaterDateTime(theaterDate, theaterStartHour, theaterStartMinute);
        LocalDateTime theaterEndDatetime = convertTheaterDateTime(theaterDate, theaterEndHour, theaterEndMinute);

        if (theaterId != null) {
            Optional<Theater> theaterOptional = theaterRepository.findById(theaterId);
            if (theaterOptional.isEmpty()) {
                throw new NullPointerException("존재하지 않는 상영관 정보입니다.");
            }
            Theater theater = theaterOptional.get();
            theater.updateTheater(
                    theaterName,
                    theaterStartDatetime,
                    theaterEndDatetime,
                    theaterStartDatetime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA),
                    theaterMaxPeople,
                    theaterMinPeople,
                    theaterCinemaName,
                    theaterCinemaBrandName,
                    theaterRegion,
                    theaterPrice,
                    theaterTime,
                    brandRepository.getBrandByBrandName(theaterCinemaBrandName)
            );

            theaterRepository.save(theater);
        } else {
            Theater theater = Theater
                    .builder()
                    .theaterName(theaterName)
                    .theaterStartDatetime(theaterStartDatetime)
                    .theaterEndDatetime(theaterEndDatetime)
                    .theaterDay(theaterStartDatetime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA))
                    .theaterMaxPeople(theaterMaxPeople)
                    .theaterMinPeople(theaterMinPeople)
                    .theaterCinemaName(theaterCinemaName)
                    .theaterCinemaBrandName(theaterCinemaBrandName)
                    .theaterRegion(theaterRegion)
                    .theaterPrice(theaterPrice)
                    .theaterTime(theaterTime)
                    .brand(brandRepository.getBrandByBrandName(theaterCinemaBrandName))
                    .build();

            theaterRepository.save(theater);
        }
    }

    @Override
    public Page<AdminUserDto> getUserPage(UserSearchCondition userSearchCondition, String startDate, String endDate, UserGrade userGrade, String userSnsKind, Pageable pageable) {
        return userRepository.getAdminUserPage(userSearchCondition, startDate, endDate, userGrade, userSnsKind, pageable);
    }

    @Override
    public void saveUser(
            Long userId,
            String userEmail,
            String userName,
            MultipartFile userImage,
            Boolean userPushYn,
            Boolean userPushNightYn,
            Boolean userUseYn,
            String userGrade
    ) {

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

            user.changeAdminUserInfo(
                    userEmail,
                    userName,
                    fileUrl,
                    userPushYn,
                    userPushNightYn,
                    userUseYn,
                    userGrade
            );

            userRepository.save(user);

        } else {

        }
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

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(
                authentication.getName(),
                "ADMIN",
                claims,
                expiredDate
        );

        return jwtAuthToken.getToken(jwtAuthToken);
    }
}
