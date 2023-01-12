package moska.rebora.Payment.Repository;

import moska.rebora.Admin.Repository.PaymentAdminRepository;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom, PaymentAdminRepository {

    Optional<Payment> getPaymentByUserRecruitment(UserRecruitment userRecruitment);
}
