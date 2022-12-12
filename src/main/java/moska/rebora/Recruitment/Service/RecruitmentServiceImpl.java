package moska.rebora.Recruitment.Service;

import com.mchange.util.DuplicateElementException;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Comment.Repository.CommentRepository;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.Service.AsyncTaskService;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.hibernate.DuplicateMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    TheaterRepository theaterRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MainBannerRepository mainBannerRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    UserMovieRepository userMovieRepository;

    @Autowired
    AsyncTaskService asyncTaskService;

    @Autowired
    PaymentService paymentService;

    /**
     * 리스트 가져오기
     *
     * @param pageable            페이징
     * @param userEmail           유저이메일
     * @param userSearchCondition 검색 조건
     * @return BasePageResponse<UserRecruitmentListDto>
     */
    @Override
    public BasePageResponse<UserRecruitmentListDto> getList(Pageable pageable, String userEmail, UserSearchCondition userSearchCondition) {

        BasePageResponse<UserRecruitmentListDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPage(recruitmentRepository.getList(pageable, userEmail, userSearchCondition));
        basePageResponse.setResult(true);

        return basePageResponse;
    }

    /**
     * 모집 정보 가져오기
     *
     * @param recruitmentId   모집 아이디
     * @param userEmail       유저 이메일
     * @param commentPageable 답글 페이징
     * @return BaseInfoResponse<RecruitmentInfoDto>
     */
    @Override
    public BaseInfoResponse<RecruitmentInfoDto> getRecruitmentInfo(
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("commentPageable") Pageable commentPageable
    ) {

        BaseInfoResponse<RecruitmentInfoDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        RecruitmentInfoDto recruitmentInfoDto = recruitmentRepository.getRecruitmentInfo(recruitmentId, userEmail);
        recruitmentInfoDto.addPageComment(commentRepository.getCommentPage(commentPageable, recruitmentId));
        recruitmentInfoDto.addUserImageList(userRecruitmentRepository.getUserImageListByRecruitment(userEmail, recruitmentId));
        baseInfoResponse.setContent(recruitmentInfoDto);

        return baseInfoResponse;
    }

    /**
     * 모집 생성
     *
     * @param movieId               영화 아이디
     * @param theaterId             극장 아이디
     * @param userEmail             유저 이메일
     * @param recruitmentIntroduce  모집 소개
     * @param userRecruitmentPeople 모집 신청 인원
     * @param bannerYn              배너 여부
     * @param bannerSubText         배너 서브 텍스트
     * @param bannerMainText        배너 메인 텍스트
     * @return Long 모집 아이디
     */
    @Override
    @Transactional
    public Long createRecruitment(Long movieId,
                                  Long theaterId,
                                  String userEmail,
                                  String recruitmentIntroduce,
                                  Integer userRecruitmentPeople,
                                  Boolean bannerYn,
                                  String bannerSubText,
                                  String bannerMainText) {

        Optional<Theater> theaterOptional = theaterRepository.findById(theaterId);
        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        User user = userRepository.getUserByUserEmail(userEmail);

        if (theaterOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 상영관입니다.");
        }

        if (movieOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 영화입니다.");
        }

        Theater theater = theaterOptional.get();
        Movie movie = movieOptional.get();
        movie.addMoviePopularCount();

        //최소 가능 날짜
        LocalDateTime recruitmentEndDate = theater.getTheaterStartDatetime().minusDays(3).toLocalDate().atTime(LocalTime.MAX);

        //모집
        Recruitment recruitment = Recruitment.builder()
                .recruitmentPeople(userRecruitmentPeople)
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .recruitmentExposeYn(true)
                .recruitmentIntroduce(recruitmentIntroduce)
                .recruitmentEndDate(recruitmentEndDate)
                .theater(theater)
                .movie(movie)
                .build();

        recruitmentRepository.save(recruitment);
        theater.addRecruitment(recruitment);

        //배너 유무 있을시에만 추가
        if (bannerYn) {
            Banner banner = Banner.builder()
                    .bannerSubText(bannerSubText)
                    .bannerMainText(bannerMainText)
                    .bannerExposeYn(true)
                    .bannerImage(movie.getMovieBannerImage())
                    .recruitment(recruitment)
                    .build();
            bannerRepository.save(banner);
            List<MainBanner> mainBannerList = mainBannerRepository.findAll();
            if (mainBannerList.size() < 10) {
                MainBanner mainBanner = MainBanner
                        .builder()
                        .banner(banner)
                        .build();

                mainBannerRepository.save(mainBanner);
            }
        }

        Boolean result = paymentService.paymentConfirmMovie(user, recruitment, userRecruitmentPeople);
        if(!result){
            throw new RuntimeException("결제 도중 오류가 발생 했습니다 다시 시도해주세요.");
        }
        theaterRepository.save(theater);
        movieRepository.save(movie);

        String notificationContent = notificationService.createNotificationContent(
                movie.getMovieName(),
                theater.getTheaterStartDatetime(),
                theater.getTheaterDay(),
                theater.getTheaterCinemaBrandName(),
                theater.getTheaterCinemaName(),
                theater.getTheaterName()
        );
        String notificationSubject = "찜한 영화의 모집게시물이 등록되었습니다.";

        asyncTaskService.createNotificationRecruitment(
                notificationSubject,
                notificationContent,
                NotificationKind.RECRUITING,
                recruitment.getId(),
                movieId
        );
        return recruitment.getId();
    }

    /**
     * 모집 신청
     *
     * @param recruitmentId         모집 PK
     * @param userEmail             유저 이메일
     * @param userRecruitmentPeople 모집 신청 인원
     */
    @Override
    public void applyRecruitment(Long recruitmentId,
                                 String userEmail,
                                 Integer userRecruitmentPeople) {

        Optional<Recruitment> recruitmentOptional = recruitmentRepository.findById(recruitmentId);


        User user = userRepository.getUserByUserEmail(userEmail);

        if (recruitmentOptional.isEmpty()) {
            throw new NullPointerException("해당 조건의 모집이 없습니다.");
        }

        Recruitment recruitment = recruitmentOptional.get();
        Theater theater = recruitment.getTheater();

        if ((recruitment.getRecruitmentPeople() + userRecruitmentPeople) > theater.getTheaterMaxPeople()) {
            throw new NullPointerException("모집 최대인원을 넘었습니다.");
        }

        recruitment.plusRecruitmentPeople(userRecruitmentPeople);

        UserRecruitment userRecruitment;

        Optional<UserRecruitment> userRecruitmentOptional = userRecruitmentRepository.getUserRecruitmentByUserAndRecruitment(user, recruitment);

        if (userRecruitmentOptional.isEmpty()) {
            userRecruitment = UserRecruitment
                    .builder()
                    .userRecruitmentWish(false)
                    .userRecruitmentYn(true)
                    .recruitment(recruitment)
                    .userRecruitmentPeople(userRecruitmentPeople)
                    .user(user)
                    .build();
        } else {
            userRecruitment = userRecruitmentOptional.get();
            if (userRecruitment.getUserRecruitmentYn()) {
                throw new DuplicateElementException("이미 신청한 모집입니다.");
            } else {
                userRecruitment.updateUserRecruitment(true, userRecruitmentPeople);
            }
        }

        userRecruitmentRepository.save(userRecruitment);
        recruitmentRepository.save(recruitment);
    }

    /**
     * 모집 신청 취소
     *
     * @param recruitmentId 모집 아이디
     * @param userEmail     유저 이메일
     */
    @Override
    public void cancelRecruitment(Long recruitmentId,
                                  String userEmail) {

        Recruitment recruitment = recruitmentRepository.getRecruitmentById(recruitmentId);
        User user = userRepository.getUserByUserEmail(userEmail);

        Optional<UserRecruitment> userRecruitmentOptional = userRecruitmentRepository.getUserRecruitmentByUserAndRecruitment(user, recruitment);

        if (userRecruitmentOptional.isEmpty()) {
            throw new NullPointerException("참여내역이 없습니다.");
        }

        if (recruitment.getRecruitmentStatus() != RecruitmentStatus.RECRUITING) {
            throw new NullPointerException("모집중인 모집만 취소 가능합니다.");
        }

        UserRecruitment userRecruitment = userRecruitmentOptional.get();
        Integer userRecruitmentPeople = userRecruitment.getUserRecruitmentPeople();

        userRecruitment.updateUserRecruitment(false, 0);
        userRecruitmentRepository.save(userRecruitment);

        recruitment.minusRecruitmentPeople(userRecruitmentPeople);
        recruitmentRepository.save(recruitment);
    }
}
