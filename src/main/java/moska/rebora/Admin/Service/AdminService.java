package moska.rebora.Admin.Service;

import moska.rebora.Admin.Dto.*;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Theater.Dto.TheaterPageDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface AdminService {

    UserLoginDto adminLogin(
            @Param("userEmail") String userEmail,
            @Param("password") String password
    );

    BasePageResponse<MoviePageDto> getAdminList(@Param("searchCondition") UserSearchCondition searchCondition,
                                                @Param("pageable") Pageable pageable);

    void changeMoviePopularCount(@Param("searchCondition") String movieAll, @Param("movieId") Long movieId);

    void adminCheck(@Param("userEmail") String userEmail);

    BaseInfoResponse<AdminMovieDto> getMovieInfo(@Param("movieId") Long movieId);

    void changeRecruitment(
            @Param("recruitmentId") Long recruitmentId,
            @Param("recruitmentStatus") RecruitmentStatus recruitmentStatus,
            @Param("recruitmentExposeYn") Boolean recruitmentExposeYn
    );

    RecruitmentInfoDto getRecruitmentInfo(@Param("recruitmentId") Long recruitmentId);

    AdminTheaterResponseDto getAdminTheaterPage(
            String theaterRegion,
            String theaterCinemaBrandName,
            LocalDate selectDate,
            Pageable pageable
    );

    AdminTheaterDto getAdminTheaterDetail(Long theaterId);

    void changeMovie(
            @Param("movieId") Long movieId,
            @Param("movieName") String movieName,
            @Param("movieRating") String movieRating,
            @Param("movieDirector") String movieDirector,
            @Param("movieStarRating") String movieStarRating,
            @Param("category") String category,
            @Param("cinema") String cinema,
            @Param("movieDetailLink") String movieDetailLink,
            @Param("movieRunningTime") Integer movieRunningTime,
            @Param("moviePrice") Integer moviePrice,
            @Param("moviePopularCount") Integer moviePopularCount,
            @Param("changeMovieImage") MultipartFile changeMovieImage,
            @Param("changeMovieBannerImage") MultipartFile changeMovieBannerImage,
            @Param("changeMovieRecruitmentImage") MultipartFile changeMovieRecruitmentImage
    );

    void theaterSave(
            @RequestParam(required = false) Long theaterId,
            @RequestParam String theaterCinemaBrandName,
            @RequestParam String theaterRegion,
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
    );

    Page<RecruitmentInfoDto> getRecruitmentPage(
            @Param("pageable") Pageable pageable,
            @Param("pageable") UserSearchCondition userSearchCondition
    );

    Page<AdminUserDto> getUserPage(
            @Param("userSearchCondition") UserSearchCondition userSearchCondition,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("userGrade") UserGrade userGrade,
            @Param("userSnsKind") String userSnsKind,
            Pageable pageable
    );

    void saveUser(
            @Param("userId") Long userId,
            @Param("userEmail") String userEmail,
            @Param("userName") String userName,
            @Param("userImage") MultipartFile userImage,
            @Param("userPushYn") Boolean userPushYn,
            @Param("userPushNightYn") Boolean userPushNightYn,
            @Param("userUseYn") Boolean userUseYn,
            @Param("userGrade") String userGrade,
            @Param("userPassword") String userPassword,
            @Param("userNickname") String userNickname
    );

    void createTheaters(MultipartFile file);

    Page<AdminPaymentDto> getPaymentPage(
            @Param("userSearchCondition") UserSearchCondition userSearchCondition,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            Pageable pageable
    );

    AdminPaymentDto getPaymentInfo(
            @Param("paymentId") String paymentId
    );

    void uploadMovieCsvFile(MultipartFile file);

    void uploadMovieImageFile(List<MultipartFile> fileList);

    void updatePaymentInfo(
            @Param("paymentId") String paymentId,
            @Param("paymentMemo") String paymentMemo
    );
}
