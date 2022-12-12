package moska.rebora.Payment.Repository;

import moska.rebora.Payment.Entity.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
}
