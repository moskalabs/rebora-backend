package moska.rebora.Payment.Repository;

import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.UserRecruitment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryCustom {

    Optional<Payment> getPaymentByUserRecruitment(UserRecruitment userRecruitment);

    List<Payment> getBatchPaymentList(
            PaymentStatus paymentStatus
    );
}
