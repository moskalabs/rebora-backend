package moska.rebora.Payment.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Service.BaseCountResponse;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Payment.Dto.CardDto;
import moska.rebora.Payment.Dto.CustomerUIdDto;
import moska.rebora.Payment.Dto.MerchantUidDto;
import moska.rebora.Payment.Entity.Card;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@Tag(name = "결제")
@RequestMapping("/api/payment")
@Slf4j
@AllArgsConstructor
public class PaymentController {
    UserRecruitmentRepository userRecruitmentRepository;
    PaymentService paymentService;
    UserRepository userRepository;
    RecruitmentRepository recruitmentRepository;

    /**
     * 모집 예약 customerUId 생성
     *
     * @param recruitmentId 모집 아이디
     * @return BaseInfoResponse<CustomerUIdDto>
     */
    @Tag(name = "결제")
    @Operation(summary = "주문번호 생성(예약 결제)")
    @PostMapping("/createCustomerUid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true),
            @ApiImplicitParam(name = "userRecruitmentPeople", value = "모집 신청 인원", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "500", description = "생성 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public BaseInfoResponse<CustomerUIdDto> createCustomerUId(
            @RequestParam Long recruitmentId,
            @RequestParam Integer userRecruitmentPeople
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByUserEmail(userEmail);

        BaseInfoResponse<CustomerUIdDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(paymentService.createCustomerUid(user.getId(), recruitmentId, userRecruitmentPeople));

        return baseInfoResponse;
    }

    @Tag(name = "결제")
    @Operation(summary = "예약 결제 빌링키 등록 완료")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRecruitmentId", value = "유저 모집 아이디", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "권한 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/reserveRecruitment/{userRecruitmentId}")
    public BaseResponse reserveRecruitment(
            @PathVariable Long userRecruitmentId
    ) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        paymentService.reserveRecruitment(userRecruitmentId);

        return baseResponse;
    }

    /**
     * 즉시 결제 주문 번호 생성
     *
     * @param recruitmentId         모집 아이디
     * @param userRecruitmentPeople 유저 모집 신청 인원
     * @return BaseResponse
     */
    @Tag(name = "결제")
    @Operation(summary = "주문 번호 생성(즉시 결제)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true),
            @ApiImplicitParam(name = "userRecruitmentPeople", value = "모집 신청 인원", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "500", description = "생성 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/createMerchantUid")
    public BaseInfoResponse<MerchantUidDto> saveMerchantUid(
            @RequestParam Long recruitmentId,
            @RequestParam(defaultValue = "0") Integer userRecruitmentPeople
    ) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByUserEmail(userEmail);

        BaseInfoResponse<MerchantUidDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(paymentService.createMerchantUid(user.getId(), recruitmentId, userRecruitmentPeople));

        return baseInfoResponse;
    }

    @Tag(name = "결제")
    @Operation(summary = "모집 생성 주문 번호 생성(즉시 결제)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "500", description = "생성 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getNewRecruitmentMerchantUid")
    public BaseInfoResponse<MerchantUidDto> getRecruitmentMerchantUid() {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getUserByUserEmail(userEmail);

        if (user == null) {
            throw new NullPointerException("유저가 존재하지 않습니다. 다시 시도해주세요.");
        }

        BaseInfoResponse<MerchantUidDto> baseInfoResponse = new BaseInfoResponse<>();
        MerchantUidDto merchantUidDto = new MerchantUidDto();
        baseInfoResponse.setResult(true);
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(user.getId());
        stringBuilder.append("_");
        stringBuilder.append("newRecruitment");
        stringBuilder.append("_");
        stringBuilder.append(now);
        merchantUidDto.setMerchantUid(stringBuilder.toString());

        baseInfoResponse.setContent(merchantUidDto);


        return baseInfoResponse;
    }

    /**
     * 즉시 결제 정보 저장
     *
     * @param userRecruitmentId 유저 아이디
     * @param merchantUid       주문 번호
     * @param impUid            거래고유번호
     * @return BaseResponse
     */
    @Tag(name = "결제")
    @Operation(summary = "즉시 결제 정보 저장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRecruitmentId", value = "유저 모집 아이디", required = true),
            @ApiImplicitParam(name = "merchantUid", value = "거래 번호", required = true),
            @ApiImplicitParam(name = "impUid", value = "아임포트 고유 아이디", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공"),
            @ApiResponse(responseCode = "500", description = "생성 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/saveImmediatePayment/{userRecruitmentId}")
    public BaseResponse saveImmediatePayment(
            @PathVariable Long userRecruitmentId,
            @RequestParam String merchantUid,
            @RequestParam String impUid
    ) {
        BaseResponse baseResponse = new BaseResponse();

        paymentService.saveImmediatePayment(userRecruitmentId, merchantUid, impUid);
        baseResponse.setResult(true);

        return baseResponse;
    }
}
