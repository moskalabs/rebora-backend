package moska.rebora.Payment.Controller;

import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class PaymentController {

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @PostMapping("/api/payment/test")
    public void test(){
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
                .paymentKey("paymentKey")
                .paymentContent("예약 결제")
                .paymentAmount(7000)
                .paymentMethod(PaymentMethod.CARD)
                .paymentStatus(PaymentStatus.COMPLETE)
                .userRecruitment(userRecruitment)
                .build();

        userRecruitment.updatePayment(payment);
        paymentRepository.save(payment);
        userRecruitmentRepository.save(userRecruitment);
    }
}
