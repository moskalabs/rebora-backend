package moska.rebora.Payment.Controller;

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
     * customerUId 생성
     *
     * @param recruitmentId 모집 아이디
     * @return BaseInfoResponse<CustomerUIdDto>
     */
    @PostMapping("/createCustomerUid")
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

    /**
     * 모집 예약 등록
     *
     * @param userRecruitmentId 유저 모집 아이디
     * @return BaseResponse
     */
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
     * 주문 번호 생성
     *
     * @param recruitmentId         모집 아이디
     * @param userRecruitmentPeople 유저 모집 신청 인원
     * @return BaseResponse
     */
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

    @GetMapping("/applyCard")
    public ModelAndView applyCard(
            ModelAndView mav,
            @RequestParam Long userRecruitmentId
    ) {

        Optional<UserRecruitment> optionalUserRecruitment = userRecruitmentRepository.findById(userRecruitmentId);

        if (optionalUserRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집 정보입니다.");
        }

        //String customerUid = paymentService.createCustomerUid(user);

        //mav.addObject("userId", user.getId());
        //mav.addObject("customerId", user.getUserCustomerId());
        //mav.addObject("customerUid", customerUid);
        mav.setViewName("payment/applyCard");

        return mav;
    }


    @GetMapping("/applyDone")
    public ModelAndView applyCardDone(
            ModelAndView mav,
            @RequestParam Long userRecruitmentId,
            @RequestParam String customerUId,
            @RequestParam Boolean imp_success,
            @RequestParam(required = false) String error_msg

    ) {
        mav.addObject("userRecruitmentId", userRecruitmentId);
        mav.addObject("customerUid", customerUId);
        mav.addObject("imp_success", imp_success);
        mav.addObject("error_msg", error_msg);

        Optional<UserRecruitment> optionalUserRecruitment = userRecruitmentRepository.findById(userRecruitmentId);

        if (optionalUserRecruitment.isEmpty()) {
            throw new NullPointerException("모집 정보가 존재하지 않습니다 다시 시도해주세요");
        }

        paymentService.applyCardUserRecruitment(optionalUserRecruitment.get(), customerUId);

        mav.setViewName("payment/applyDone");
        return mav;
    }


    @PostMapping("/refundPayment/{userRecruitmentId}")
    @Transactional
    public BaseResponse refundPayment(
            @PathVariable Long userRecruitmentId
    ) {
        Optional<UserRecruitment> userRecruitmentOptional = userRecruitmentRepository.findById(userRecruitmentId);

        if (userRecruitmentOptional.isEmpty()) {
            throw new NullPointerException("존재 하지 않는 정보입니다. 다시 시도해주세요");
        }
        UserRecruitment userRecruitment = userRecruitmentOptional.get();
        Payment payment = userRecruitment.getPayment();

        if (payment == null) {
            throw new NullPointerException("결재내역이 존재하지 않습니다. 다시 시도해주세요");
        }

        BaseResponse baseResponse = new BaseResponse();
//                paymentService.paymentCancel(userRecruitment);
//        if (baseResponse.getResult()) {
//            Recruitment recruitment = userRecruitment.getRecruitment();
//            recruitment.minusRecruitmentPeople(userRecruitment.getUserRecruitmentPeople());
//            recruitmentRepository.save(recruitment);
//        }

        return baseResponse;
    }

    @GetMapping("/getCardUseCount/{userId}")
    public BaseCountResponse getCardUseCount(
            @PathVariable Long userId
    ) {
        BaseCountResponse baseCountResponse = new BaseCountResponse();
        baseCountResponse.setCount(paymentService.CardUseCount(userId));
        baseCountResponse.setResult(true);
        return baseCountResponse;
    }

    @GetMapping("/getCard/{userId}")
    public BaseInfoResponse<CardDto> getCard(
            @PathVariable Long userId
    ) {
        BaseInfoResponse<CardDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(paymentService.getCard(userId));

        return baseInfoResponse;
    }

    @PostMapping("/deleteCard/{userId}")
    public BaseResponse deleteCard(
            @PathVariable Long userId
    ) {
        BaseResponse baseResponse = new BaseResponse();
        paymentService.deleteCard(userId);
        baseResponse.setResult(true);

        return baseResponse;
    }
}
