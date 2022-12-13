package moska.rebora.Payment.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Service.BaseCountResponse;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Dto.CardDto;
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
public class PaymentController {

    @Value("${toss.payments.test.client-key}")
    private String TossClientKey;
    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentService paymentService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @GetMapping("/applyCard")
    public ModelAndView applyCard(
            ModelAndView mav,
            @RequestParam Long userId
    ) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저입니다.");
        }

        User user = userOptional.get();
        String customerUid = paymentService.createCustomerUid(user);

        mav.addObject("userId", user.getId());
        mav.addObject("customerId", user.getUserCustomerId());
        mav.addObject("customerUid", customerUid);
        mav.setViewName("payment/applyCard");

        return mav;
    }

    @GetMapping("/applyDone")
    public ModelAndView applyCardDone(
            ModelAndView mav,
            @RequestParam Long userId,
            @RequestParam String customerUId,
            @RequestParam Boolean imp_success,
            @RequestParam(required = false) String error_msg

    ) {
        mav.addObject("userId", userId);
        mav.addObject("customerUid", customerUId);
        mav.addObject("imp_success", imp_success);
        mav.addObject("error_msg", error_msg);
        mav.setViewName("payment/applyDone");
        return mav;
    }


    @GetMapping("/applyCardSuccess")
    public BaseResponse paymentPageFail(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String customerUid
    ) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저입니다.");
        }

        paymentService.applyCard(userOptional.get(), customerUid);
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PostMapping("/paymentConfirmMovie/{userId}/{recruitmentId}")
    public BaseResponse paymentConfirmMovie(
            @PathVariable Long userId,
            @PathVariable Long recruitmentId,
            @RequestParam Integer userRecruitmentPeople
    ) {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(recruitmentId);

        if (optionalUser.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저입니다. 다시 시도해주세요.");
        }

        if (optionalRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집입니다. 다시 시도해주세요.");
        }


        return paymentService.paymentConfirmMovie(optionalUser.get(), optionalRecruitment.get(), userRecruitmentPeople);
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

        BaseResponse baseResponse = paymentService.paymentCancel(payment);
        if (baseResponse.getResult()) {
            Recruitment recruitment = userRecruitment.getRecruitment();
            recruitment.minusRecruitmentPeople(userRecruitment.getUserRecruitmentPeople());
            recruitmentRepository.save(recruitment);
        }

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
