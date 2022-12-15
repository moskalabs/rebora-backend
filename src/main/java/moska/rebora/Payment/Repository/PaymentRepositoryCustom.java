package moska.rebora.Payment.Repository;

import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;

import java.util.List;

public interface PaymentRepositoryCustom {

    public List<Payment> getBatchPaymetList(
            PaymentStatus paymentStatus
    );
}
