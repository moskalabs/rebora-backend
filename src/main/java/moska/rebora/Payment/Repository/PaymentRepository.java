package moska.rebora.Payment.Repository;

import moska.rebora.Payment.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
