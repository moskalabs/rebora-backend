package moska.rebora.Admin.Controller;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.AdminPaymentDto;
import moska.rebora.Admin.Dto.AdminUserDto;
import moska.rebora.Admin.Dto.FileReadCsvDto;
import moska.rebora.Admin.Service.AdminCinemaService;
import moska.rebora.Admin.Service.AdminService;
import moska.rebora.Cinema.Dto.CinemaPageDto;
import moska.rebora.Cinema.Dto.MovieCinemaDto;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Cinema.Repository.BrandRepository;
import moska.rebora.Cinema.Repository.MovieCinemaRepository;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Service.S3Service;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final MovieCinemaRepository movieCinemaRepository;

    AdminService adminService;

    UserRepository userRepository;

    S3Service s3Service;

    BrandRepository brandRepository;

    AdminCinemaService adminCinemaService;

    @GetMapping("/login")
    public ModelAndView login(
            ModelAndView mav
    ) {

        mav.setViewName("/admin/login");

        return mav;
    }

    @PostMapping("/adminLogin")
    public UserLoginDto adminLogin(
            @RequestParam String userEmail,
            @RequestParam String password
    ) {
        return adminService.adminLogin(userEmail, password);
    }

    @GetMapping("/movie/list")
    public ModelAndView movieList(
            ModelAndView mav,
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(defaultValue = "movieName") String searchCondition,
            @RequestParam(defaultValue = "") String searchWord
    ) {
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setSearchCondition(searchCondition);
        userSearchCondition.setSearchWord(searchWord);

        mav.addObject("movieList", adminService.getAdminList(userSearchCondition, pageable));
        mav.setViewName("/admin/movie/list");

        return mav;
    }

    @GetMapping("/movie/info")
    public ModelAndView movieInfo(
            ModelAndView mav,
            @RequestParam(required = false) Long movieId
    ) {

        mav.addObject("response", adminService.getMovieInfo(movieId));
        mav.setViewName("/admin/movie/info");

        return mav;
    }

    @GetMapping("/recruitment/list")
    public ModelAndView recruitmentList(
            ModelAndView mav,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(defaultValue = "movieName") String searchCondition,
            @RequestParam(defaultValue = "") String searchWord
    ) {

        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setSearchCondition(searchCondition);
        userSearchCondition.setSearchWord(searchWord);

        BasePageResponse<RecruitmentInfoDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(adminService.getRecruitmentPage(pageable, userSearchCondition));

        mav.addObject("recruitmentList", basePageResponse);
        mav.setViewName("/admin/recruitment/list");

        return mav;
    }

    @GetMapping("/recruitment/info")
    public ModelAndView recruitmentInfo(
            ModelAndView mav,
            @RequestParam(required = false) Long recruitmentId
    ) {
        BaseInfoResponse<RecruitmentInfoDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(adminService.getRecruitmentInfo(recruitmentId));

        mav.addObject("recruitment", baseInfoResponse);
        mav.setViewName("/admin/recruitment/info");

        return mav;
    }

    @GetMapping("/theater/list")
    public ModelAndView theaterList(
            ModelAndView mav,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(defaultValue = "서울") String theaterRegion,
            @RequestParam(defaultValue = "CGV") String theaterCinemaBrandName,
            @RequestParam(defaultValue = "") String selectDate
    ) {
        LocalDate date = LocalDate.now();

        if (!selectDate.equals("")) {
            date = LocalDate.parse(selectDate);
        }

        mav.addObject("theaterList", adminService.getAdminTheaterPage(theaterRegion, theaterCinemaBrandName, date, pageable));
        mav.setViewName("/admin/theater/list");
        return mav;
    }

    @GetMapping("/payment/list")
    public ModelAndView paymentList(
            ModelAndView mav,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(defaultValue = "") String searchCondition,
            @RequestParam(defaultValue = "") String searchWord,
            @RequestParam(defaultValue = "") String toDate,
            @RequestParam(defaultValue = "") String fromDate
    ) {
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setSearchCondition(searchCondition);
        userSearchCondition.setSearchWord(searchWord);

        BasePageResponse<AdminPaymentDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(adminService.getPaymentPage(userSearchCondition, fromDate, toDate, pageable));

        mav.addObject("paymentList", basePageResponse);
        mav.setViewName("/admin/payment/list");
        return mav;
    }

    @GetMapping("/payment/info/{paymentId}")
    public ModelAndView paymentInfo(
            @PathVariable String paymentId,
            ModelAndView mav
    ) {
        BaseInfoResponse<AdminPaymentDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(adminService.getPaymentInfo(paymentId));
        mav.addObject("payment", baseInfoResponse);
        mav.setViewName("/admin/payment/info");
        return mav;
    }

    @GetMapping("/theater/info/{theaterId}")
    public ModelAndView theaterList(
            ModelAndView mav,
            @PathVariable Long theaterId
    ) {
        List<String> regionList = new ArrayList<>(Arrays.asList("서울", "경기", "인천", "강원", "대전/충청", "대구", "부산/울산", "경상", "광주/전라/제주"));
        List<Brand> brandList = brandRepository.findAll();
        mav.addObject("theater", adminService.getAdminTheaterDetail(theaterId));
        mav.addObject("regionList", regionList);
        mav.addObject("brandList", brandList);
        mav.setViewName("/admin/theater/info");
        return mav;
    }

    @PostMapping("/theater/theaterSave")
    public BaseResponse theaterSave(
            @RequestParam(required = false) Long theaterId,
            @RequestParam(required = false) String theaterCinemaBrandName,
            @RequestParam(required = false) String theaterRegion,
            @RequestParam String theaterCinemaName,
            @RequestParam String theaterName,
            @RequestParam String theaterDate,
            @RequestParam String theaterStartHour,
            @RequestParam String theaterStartMinute,
            @RequestParam String theaterEndHour,
            @RequestParam String theaterEndMinute,
            @RequestParam Integer theaterTime,
            @RequestParam Integer theaterPrice,
            @RequestParam Integer theaterMinPeople,
            @RequestParam Integer theaterMaxPeople
    ) {
        adminService.theaterSave(
                theaterId,
                theaterCinemaBrandName,
                theaterRegion,
                theaterCinemaName,
                theaterName,
                theaterDate,
                theaterStartHour,
                theaterStartMinute,
                theaterEndHour,
                theaterEndMinute,
                theaterTime,
                theaterPrice,
                theaterMinPeople,
                theaterMaxPeople
        );

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PostMapping("/recruitment/changeRecruitment/{recruitmentId}")
    public BaseResponse changeRecruitment(
            @PathVariable Long recruitmentId,
            @RequestParam String recruitmentStatus,
            @RequestParam Boolean recruitmentExposeYn
    ) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        adminService.changeRecruitment(recruitmentId, RecruitmentStatus.valueOf(recruitmentStatus), recruitmentExposeYn);

        return baseResponse;
    }


    @PostMapping("/movie/changeMovie")
    public BaseResponse changeMovie(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String movieName,
            @RequestParam(required = false) String movieRating,
            @RequestParam(required = false) String movieDirector,
            @RequestParam(required = false) String movieStarRating,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String cinema,
            @RequestParam(required = false) String movieDetailLink,
            @RequestParam(required = false) Integer movieRunningTime,
            @RequestParam(required = false) Integer moviePrice,
            @RequestParam(required = false) Integer moviePopularCount,
            @RequestParam(required = false) MultipartFile changeMovieImage,
            @RequestParam(required = false) MultipartFile changeMovieBannerImage,
            @RequestParam(required = false) MultipartFile changeMovieRecruitmentImage
    ) {

        adminService.changeMovie(
                movieId,
                movieName,
                movieRating,
                movieDirector,
                movieStarRating,
                category,
                cinema,
                movieDetailLink,
                movieRunningTime,
                moviePopularCount,
                moviePrice,
                changeMovieImage,
                changeMovieBannerImage,
                changeMovieRecruitmentImage
        );

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PostMapping("/changeMoviePopularCount")
    public BaseResponse changeMoviePopularCount(
            @RequestParam(required = false, defaultValue = "") String movieAll,
            @RequestParam(required = false) Long movieId
    ) {
        BaseResponse baseResponse = new BaseResponse();
        adminService.changeMoviePopularCount(movieAll, movieId);
        baseResponse.setResult(true);
        return baseResponse;
    }

    @GetMapping("/user/list")
    public ModelAndView userList(
            ModelAndView mav,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(defaultValue = "") String startDate,
            @RequestParam(defaultValue = "") String endDate,
            @RequestParam(defaultValue = "") String userGrade,
            @RequestParam(defaultValue = "ALL") String userSnsKind,
            @RequestParam(defaultValue = "userEmail") String searchCondition,
            @RequestParam(defaultValue = "") String searchWord
    ) {
        UserGrade grade = null;
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setSearchCondition(searchCondition);

        if (!userGrade.equals("")) {
            grade = UserGrade.valueOf(userGrade);
        }

        if (!searchWord.equals("")) {
            userSearchCondition.setSearchWord(searchWord);
        }

        BasePageResponse<AdminUserDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPage(adminService.getUserPage(userSearchCondition, startDate, endDate, grade, userSnsKind, pageable));
        basePageResponse.setResult(true);
        mav.addObject("userList", basePageResponse);
        mav.setViewName("/admin/user/list");
        return mav;
    }

    @GetMapping("/user/info")
    public ModelAndView userInfo(
            ModelAndView mav,
            @RequestParam(required = false) Long userId
    ) {
        BaseInfoResponse<AdminUserDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);

        if (userId != null) {
            baseInfoResponse.setContent(userRepository.getAdminUserInfo(userId));
        }

        mav.addObject("user", baseInfoResponse);
        mav.setViewName("/admin/user/info");
        return mav;
    }

    @PostMapping("/user/saveUser")
    public BaseResponse saveUser(
            @RequestParam(required = false) Long userId,
            @RequestParam String userEmail,
            @RequestParam String userName,
            @RequestParam(required = false) MultipartFile userImage,
            @RequestParam Boolean userPushYn,
            @RequestParam Boolean userPushNightYn,
            @RequestParam Boolean userUseYn,
            @RequestParam String userGrade,
            @RequestParam(required = false, defaultValue = "") String userPassword,
            @RequestParam String userNickname
    ) {
        adminService.saveUser(userId, userEmail, userName, userImage, userPushYn, userPushNightYn, userUseYn, userGrade, userPassword, userNickname);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        return baseResponse;
    }

    @PutMapping("/user/updateUseYn/{userId}")
    public BaseResponse updateUseYn(
            @PathVariable Long userId,
            @RequestParam Boolean userUseYn
    ) {
        User user = userRepository.getUserById(userId);
        user.changeUseYn(userUseYn);
        userRepository.save(user);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
    }

    @DeleteMapping("/user/deleteUser/{userId}")
    public BaseResponse updateUseYn(
            @PathVariable Long userId
    ) {
        User user = userRepository.getUserById(userId);
        userRepository.delete(user);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
    }

    @GetMapping("/adminCheck")
    public BaseResponse adminCheck() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("userEmail={}", userEmail);
        adminService.adminCheck(userEmail);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PostMapping("/theater/readCsvFile")
    public BaseResponse theaterReadCsvFile(MultipartFile file) throws Exception {

        BaseResponse baseResponse = new BaseResponse();

        try {
            adminService.createTheaters(file);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        baseResponse.setResult(true);
        return baseResponse;
    }

    @GetMapping("/theater/downloadCsvFile")
    public ResponseEntity<byte[]> download() throws IOException {
        return s3Service.getObject("default/theaterCsv.csv");
    }

    @GetMapping("/testInfo")
    public ModelAndView testInfo(
            ModelAndView mav
    ) {
        mav.setViewName("/admin/payment/infoTest");

        return mav;
    }

    @PostMapping("/movie/uploadCsvFile")
    public BaseResponse movieUploadCsv(
            MultipartFile file
    ) {
        BaseResponse baseResponse = new BaseResponse();
        adminService.uploadMovieCsvFile(file);
        baseResponse.setResult(true);
        return baseResponse;
    }

    @GetMapping("/movie/downloadCsvFile")
    public ResponseEntity<byte[]> movieDownload() throws IOException {
        return s3Service.getObject("default/movieCsv.csv");
    }

    @PostMapping("/movie/uploadImageFolder")
    public BaseResponse uploadImageFolder(
            List<MultipartFile> files
    ) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        adminService.uploadMovieImageFile(files);
        return baseResponse;
    }

    @PostMapping("/payment/updatePaymentInfo/{paymentId}")
    public BaseResponse updatePaymentInfo(
            @PathVariable String paymentId,
            @RequestParam String paymentMemo
    ) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        adminService.updatePaymentInfo(paymentId, paymentMemo);

        return baseResponse;
    }

    @GetMapping("/cinema/list")
    public ModelAndView getCinemaList(
            ModelAndView mav,
            @PageableDefault Pageable pageable,
            @RequestParam(defaultValue = "", required = false) String searchWord,
            @RequestParam(defaultValue = "", required = false) String searchCondition,
            @RequestParam(defaultValue = "", required = false) String cinemaBrand
    ) {
        log.info("cinemaBrand={}", cinemaBrand);
        log.info("searchWord={}", searchWord);
        log.info("searchCondition={}", searchCondition);

        BasePageResponse<CinemaPageDto> basePageResponse = new BasePageResponse<>();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setSearchWord(searchWord);
        userSearchCondition.setSearchCondition(searchCondition);
        userSearchCondition.setCinemaBrand(cinemaBrand);

        basePageResponse.setPage(adminCinemaService.getCinemaPage(pageable, userSearchCondition));
        basePageResponse.setResult(true);

        mav.addObject("cinemaList", basePageResponse);
        mav.setViewName("/admin/cinema/list");

        return mav;
    }

    @GetMapping("/cinema/info")
    public ModelAndView getCinemaInfo(
            ModelAndView mav,
            @RequestParam(required = false) Long cinemaId
    ) {
        BaseInfoResponse<CinemaPageDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(adminCinemaService.getCinemaInfo(cinemaId));

        mav.setViewName("/admin/cinema/info");
        mav.addObject("cinema", baseInfoResponse);
        return mav;
    }

    @PostMapping("/cinema/saveInfo")
    public BaseResponse saveInfo(
            Long cinemaId,
            String brandName,
            String regionName,
            String cinemaName,
            @RequestParam(value = "movieList") String[] movieList
    ) {

        BaseResponse baseResponse = new BaseResponse();
        log.info("cinemaId={}", cinemaId);
        log.info("brandName={}", brandName);
        log.info("regionName={}", regionName);
        log.info("cinemaName={}", cinemaName);

        String finalString = "";
        List<Long> movieIdList = new ArrayList<>();
        log.info("movieList Length={}", movieList.length);
        for (String s : movieList) {
            if (s.equals("[]")) {
                log.info("movieId={}", s);
                continue;
            }
            finalString = s.replace("[", "");
            finalString = finalString.replace("\"", "");
            finalString = finalString.replace("]", "");
            log.info("movieName={}", Long.valueOf(finalString));
            movieIdList.add(Long.valueOf(finalString));
        }

        adminCinemaService.saveInfo(cinemaId, brandName, regionName, cinemaName, movieIdList);

        baseResponse.setResult(true);

        return baseResponse;
    }
}
