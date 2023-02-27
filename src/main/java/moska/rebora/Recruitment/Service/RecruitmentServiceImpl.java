package moska.rebora.Recruitment.Service;

import com.mchange.util.DuplicateElementException;
import lombok.AllArgsConstructor;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Banner.Service.BannerService;
import moska.rebora.Comment.Repository.CommentRepository;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Service.AsyncTaskService;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Dto.ReserveRecruitmentDto;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentCustom;
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
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
@AllArgsConstructor
public class RecruitmentServiceImpl implements RecruitmentService {

    RecruitmentRepository recruitmentRepository;
    CommentRepository commentRepository;
    UserRecruitmentRepository userRecruitmentRepository;
    TheaterRepository theaterRepository;
    UserRepository userRepository;
    BannerRepository bannerRepository;
    MovieRepository movieRepository;
    MainBannerRepository mainBannerRepository;
    NotificationService notificationService;
    UserMovieRepository userMovieRepository;
    AsyncTaskService asyncTaskService;
    BannerService bannerService;
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

        String userBirth = LocalDate.now().minusDays(20L).toString();
        if (!userEmail.equals("anonymousUser")) {
            User user = userRepository.getUserByUserEmail(userEmail);
            userBirth = user.getUserBirth();
        }

        basePageResponse.setPage(recruitmentRepository.getList(pageable, userEmail, userBirth, userSearchCondition));
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
     * @param userRecruitmentPeople 모집 신청 인원
     * @param userEmail             유저 이메일
     * @param movieId               영화 이이디
     * @param theaterId             상영관 라이디
     * @param recruitmentIntroduce  모집 소개글
     * @param bannerYn              배너 유무
     * @param bannerSubText         배너 서브 글
     * @param bannerMainText        배너 메인 글
     * @param merchantUid           상품 아이디
     * @param impUid                결제 아이디
     */
    @Override
    @Transactional
    public Recruitment createRecruitment(
            @Param("userRecruitmentPeople") Integer userRecruitmentPeople,
            @Param("userEmail") String userEmail,
            @Param("movieId") Long movieId,
            @Param("theaterId") Long theaterId,
            @Param("recruitmentIntroduce") String recruitmentIntroduce,
            @Param("bannerYn") Boolean bannerYn,
            @Param("bannerSubText") String bannerSubText,
            @Param("bannerMainText") String bannerMainText,
            @Param("recruitmentCommentUseYn") Boolean recruitmentCommentUseYn,
            @Param("merchantUid") String merchantUid,
            @Param("impUid") String impUid
    ) {
        User user = userRepository.getUserByUserEmail(userEmail);
        Optional<Theater> optionalTheater = theaterRepository.findById(theaterId);
        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        if (optionalTheater.isEmpty()) {
            JSONObject jsonObject = paymentService.refundPayment(impUid);
            Long code = (Long) jsonObject.get("code");
            if (code != 0L) {
                throw new NullPointerException("존재하지 않는 상영관입니다. 환불이 완료되지 않았습니다 관리자에게 문의해주세요");
            } else {
                JSONObject responseTheater = (JSONObject) jsonObject.get("response");
                String status = (String) responseTheater.get("status");
                if (status.equals("failed")) {
                    throw new NullPointerException("존재하지 않는 상영관입니다. 환불이 완료되지 않았습니다 관리자에게 문의해주세요");
                } else {
                    throw new NullPointerException("존재하지 않는 상영관입니다. 환불 완료되었습니다.");
                }
            }
        }

        Theater theater = optionalTheater.get();

        if (theater.getRecruitment() != null) {
            JSONObject jsonObject = paymentService.refundPayment(impUid);
            Long code = (Long) jsonObject.get("code");
            if (code != 0L) {
                throw new NullPointerException("이미 모집된 상영관입니다. 환불이 완료되지 않았습니다 관리자에게 문의해주세요");
            } else {
                JSONObject responseRecruitment = (JSONObject) jsonObject.get("response");
                String status = (String) responseRecruitment.get("status");
                if (status.equals("failed")) {
                    throw new NullPointerException("이미 모집된 상영관입니다. 환불이 완료되지 않았습니다 관리자에게 문의해주세요");
                } else {
                    throw new NullPointerException("이미 모집된 상영관입니다. 환불 완료되었습니다.");
                }
            }
        }

        if (movieOptional.isEmpty()) {
            JSONObject jsonObject = paymentService.refundPayment(impUid);
            Long code = (Long) jsonObject.get("code");
            if (code != 0L) {
                throw new NullPointerException("존재하지 않는 영화입니다. 환불이 완료되지 않았습니다 관리자에게 문의해주세요");
            } else {
                JSONObject responseMovie = (JSONObject) jsonObject.get("response");
                String status = (String) responseMovie.get("status");
                if (status.equals("failed")) {
                    throw new NullPointerException("존재하지 않는 영화입니다. 환불이 완료되지 않았습니다 관리자에게 문의해주세요");
                } else {
                    throw new NullPointerException("존재하지 않는 영화입니다. 환불 완료되었습니다.");
                }
            }
        }

        Movie movie = movieRepository.getMovieById(movieId);

        LocalDateTime recruitmentEndDate = theater.getTheaterStartDatetime().minusDays(3).toLocalDate().atStartOfDay().plusSeconds(1L);

        //모집
        Recruitment recruitment = Recruitment.builder()
                .recruitmentPeople(userRecruitmentPeople)
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .recruitmentExposeYn(true)
                .recruitmentIntroduce(recruitmentIntroduce)
                .recruitmentEndDate(recruitmentEndDate)
                .recruitmentCommentUseYn(recruitmentCommentUseYn)
                .theater(theater)
                .movie(movie)
                .build();

        recruitmentRepository.save(recruitment);

        theater.addRecruitment(recruitment);
        theaterRepository.save(theater);

        movie.addMoviePopularCount();

        String content = user.getUserNickname() + "님의 모집 영화(" + movie.getMovieName() + ")의 " + userRecruitmentPeople + "명 즉시 결제";

        //결제
        JSONObject response = paymentService.getPaymentByMerchantUid(impUid);

        Long amount = (Long) response.get("amount"); //결제금액
        Long paidAt = (Long) response.get("paid_at"); //결제 시각
        String cardNumber = (String) response.get("card_number"); //카드 번호
        String cardName = (String) response.get("card_name"); //카드사
        String cardCode = (String) response.get("card_code"); //카드 코드
        String payMethod = (String) response.get("pay_method"); //결제 방법
        String pgProvider = (String) response.get("pg_provider"); //PG사
        String receiptUrl = (String) response.get("receipt_url"); //영수증 URL
        String status = (String) response.get("status"); //결제 상태
        String failReason = (String) response.get("fail_reason"); //실패 이유

        if (status.equals("failed")) {
            throw new RuntimeException("결제가 실패했습니다. 다시 시도해주세요. 실패 이유 : " + failReason);
        }

        //결제 시기
        LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAt), TimeZone.getDefault().toZoneId());

        UserRecruitment userRecruitment = UserRecruitment
                .builder()
                .user(user)
                .recruitment(recruitment)
                .userRecruitmentYn(true)
                .userRecruitmentPeople(userRecruitmentPeople)
                .userRecruitmentWish(false)
                .build();

        userRecruitmentRepository.save(userRecruitment);

        Payment payment = paymentService.createPayment(Math.toIntExact(amount), cardNumber, cardName, cardCode, payMethod, pgProvider, receiptUrl, content, false, userRecruitment, merchantUid, impUid, PaymentStatus.COMPLETE, authenticatedAt);

        movieRepository.save(movie);

        String notificationContent = notificationService.createNotificationContent(
                theater.getTheaterStartDatetime(),
                theater.getTheaterDay(),
                theater.getTheaterCinemaBrandName(),
                theater.getTheaterCinemaName(),
                theater.getTheaterName()
        );

        notificationService.createNotificationPayment(
                "모집의 결제가 완료되었습니다.",
                notificationContent,
                NotificationKind.RECRUITMENT,
                movie.getMovieName(),
                user,
                recruitment,
                payment
        );

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

            if (mainBannerList.size() < 30) {
                MainBanner mainBanner = MainBanner
                        .builder()
                        .banner(banner)
                        .build();

                mainBannerRepository.save(mainBanner);
            }
        }

        return recruitment;
    }

//    @Override
//    public ReserveRecruitmentDto reserveRecruitment(Long movieId, Long theaterId, String userEmail, String recruitmentIntroduce, Boolean bannerYn, String bannerSubText, String bannerMainText) {
//
//        ReserveRecruitmentDto reserveRecruitmentDto = new ReserveRecruitmentDto();
//        Optional<Theater> theaterOptional = theaterRepository.findById(theaterId);
//        Optional<Movie> movieOptional = movieRepository.findById(movieId);
//
//        User user = userRepository.getUserByUserEmail(userEmail);
//
//        if (theaterOptional.isEmpty()) {
//            throw new NullPointerException("존재하지 않는 상영관입니다.");
//        }
//
//        if (movieOptional.isEmpty()) {
//            throw new NullPointerException("존재하지 않는 영화입니다.");
//        }
//
//        Theater theater = theaterOptional.get();
//        Movie movie = movieOptional.get();
//
//        if (theater.getRecruitment() != null) {
//            throw new RuntimeException("이미 예약되어 있거나 모집중인 영화 입니다.");
//        }
//
//        //모집 종료 날짜
//        LocalDateTime recruitmentEndDate = theater.getTheaterStartDatetime().minusDays(3).toLocalDate().atStartOfDay().plusSeconds(1L);
//
//        //모집
//        Recruitment recruitment = Recruitment.builder()
//                .recruitmentPeople(0)
//                .recruitmentStatus(RecruitmentStatus.WAIT)
//                .recruitmentExposeYn(false)
//                .recruitmentIntroduce(recruitmentIntroduce)
//                .recruitmentEndDate(recruitmentEndDate)
//                .theater(theater)
//                .movie(movie)
//                .build();
//
//        recruitmentRepository.save(recruitment);
//
//        //배너 유무 있을시에만 추가
//        if (bannerYn) {
//            Banner banner = Banner.builder()
//                    .bannerSubText(bannerSubText)
//                    .bannerMainText(bannerMainText)
//                    .bannerExposeYn(false)
//                    .bannerImage(movie.getMovieBannerImage())
//                    .recruitment(recruitment)
//                    .build();
//            bannerRepository.save(banner);
//            List<MainBanner> mainBannerList = mainBannerRepository.findAll();
//            if (mainBannerList.size() < 10) {
//                MainBanner mainBanner = MainBanner
//                        .builder()
//                        .banner(banner)
//                        .build();
//
//                mainBannerRepository.save(mainBanner);
//            }
//        }
//
//        theater.addRecruitment(recruitment);
//        theaterRepository.save(theater);
//
//        reserveRecruitmentDto.setRecruitmentId(recruitment.getId());
//        reserveRecruitmentDto.setMerchantUid(paymentService.createPaymentId(user.getId(), recruitment.getId()));
//        reserveRecruitmentDto.setTheaterPrice(movie.getMoviePrice() + theater.getTheaterPrice());
//
//        return reserveRecruitmentDto;
//    }

    @Override
    public void cancelReserve(Long recruitmentId) {
        Optional<Recruitment> recruitmentOptional = recruitmentRepository.findById(recruitmentId);
        if (recruitmentOptional.isEmpty()) {
            throw new NullPointerException("존재 하지 않는 모집입니다.");
        }

        Recruitment recruitment = recruitmentOptional.get();
        Banner banner = bannerRepository.getBannerByRecruitment(recruitment);
        bannerService.bannerDelete(banner);

        Theater theater = recruitment.getTheater();
        theater.addRecruitment(null);

        theaterRepository.save(theater);
        recruitmentRepository.delete(recruitment);
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

        Optional<Payment> optionalPayment = Optional.ofNullable(userRecruitment.getPayment());

        if (optionalPayment.isEmpty()) {

            if (Boolean.TRUE.equals(userRecruitment.getUserRecruitmentYn())) {
                Integer userRecruitmentPeople = userRecruitment.getUserRecruitmentPeople();

                userRecruitment.updateUserRecruitment(false, 0);
                userRecruitmentRepository.save(userRecruitment);

                recruitment.minusRecruitmentPeople((recruitment.getRecruitmentPeople() - userRecruitmentPeople));
                recruitmentRepository.save(recruitment);
            }
        } else {
            Payment payment = optionalPayment.get();
            if (payment.getPaymentStatus().equals(PaymentStatus.COMPLETE) && Boolean.TRUE.equals(userRecruitment.getUserRecruitmentYn())) {

                paymentService.paymentCancel(userRecruitment);

                Integer userRecruitmentPeople = userRecruitment.getUserRecruitmentPeople();

                userRecruitment.updateUserRecruitment(false, 0);
                userRecruitmentRepository.save(userRecruitment);

                recruitment.minusRecruitmentPeople((recruitment.getRecruitmentPeople() - userRecruitmentPeople));
                recruitmentRepository.save(recruitment);
            }
        }
    }

    /**
     * 모집 업데이트
     *
     * @param recruitmentId        모집 아이디
     * @param recruitmentIntroduce 모집 소개
     */
    @Override
    @Transactional
    public void updateRecruitment(
            Long recruitmentId,
            String userEmail,
            String recruitmentIntroduce
    ) {
        User user = userRepository.getUserByUserEmail(userEmail);

        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(recruitmentId);

        //모집이 없을 경우
        if (optionalRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집 정보입니다.");
        }

        Recruitment recruitment = optionalRecruitment.get();

        //유저가 권한이 있는지
        if (!user.getUserGrade().equals(UserGrade.ADMIN) && !recruitment.getCreatedBy().equals(userEmail)) {
            throw new RuntimeException("모집 수정 권한이 없는 유저입니다.");
        }

        //모집 변경
        recruitment.changeRecruitment(recruitmentIntroduce);
//        Movie movie = recruitment.getMovie();
//        Banner banner = bannerRepository.getBannerByRecruitment(recruitment);

        //배너가 있는 모집일 경우
//        if (banner != null) {
//            banner.changeBanner(bannerYn, bannerMainText, bannerSubText);
//
//            MainBanner mainBanner = mainBannerRepository.getMainBannerByBanner(banner);
//
//            //배너가 노출일 경우
//            if (Boolean.FALSE.equals(bannerYn)) {
//
//                //배너 안보이게 할 경우 메인 배너에서 삭제
//                if (mainBanner != null) {
//                    mainBannerRepository.delete(mainBanner);
//                }
//
//            } else {
//
//                //메인 배너가 없을 경우 생성
//                if (mainBanner == null) {
//
//                    List<MainBanner> mainBannerList = mainBannerRepository.findAll();
//
//                    if (mainBannerList.size() < 30) {
//                        MainBanner createMainBanner = MainBanner
//                                .builder()
//                                .banner(banner)
//                                .build();
//
//                        mainBannerRepository.save(createMainBanner);
//                    }
//                }
//            }
//
//            bannerRepository.save(banner);
//        } else {
//
//            //배너 노출할 경우
//            if (Boolean.TRUE.equals(bannerYn)) {
//                Banner createBanner = Banner
//                        .builder()
//                        .bannerExposeYn(true)
//                        .bannerImage(movie.getMovieBannerImage())
//                        .bannerMainText(bannerMainText)
//                        .bannerSubText(bannerSubText)
//                        .recruitment(recruitment)
//                        .build();
//
//                bannerRepository.save(createBanner);
//
//                List<MainBanner> mainBannerList = mainBannerRepository.findAll();
//
//                if (mainBannerList.size() < 30) {
//                    MainBanner createMainBanner = MainBanner
//                            .builder()
//                            .banner(createBanner)
//                            .build();
//
//                    mainBannerRepository.save(createMainBanner);
//                }
//            }
//        }

        recruitmentRepository.save(recruitment);

    }

    @Override
    public void updateRecruitmentCommentUse(Long recruitmentId, String userEmail, Boolean recruitmentCommentUseYn) {
        User user = userRepository.getUserByUserEmail(userEmail);

        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(recruitmentId);

        //모집이 없을 경우
        if (optionalRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집 정보입니다.");
        }

        Recruitment recruitment = optionalRecruitment.get();

        //유저가 권한이 있는지
        if (!user.getUserGrade().equals(UserGrade.ADMIN) && !recruitment.getCreatedBy().equals(userEmail)) {
            throw new RuntimeException("모집 수정 권한이 없는 유저입니다.");
        }

        //모집 변경
        recruitment.updateCommentUse(recruitmentCommentUseYn);

        recruitmentRepository.save(recruitment);
    }
}
