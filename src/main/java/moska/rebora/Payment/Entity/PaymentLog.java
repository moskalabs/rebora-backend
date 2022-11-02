package moska.rebora.Payment.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.PaymentStatus;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_log_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String paymentLogContent;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus paymentLogStatus;

    @Column(nullable = false)
    private Integer paymentLogAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public PaymentLog(String paymentLogContent, PaymentStatus paymentLogStatus, Integer paymentLogAmount, Payment payment) {
        this.paymentLogContent = paymentLogContent;
        this.paymentLogStatus = paymentLogStatus;
        this.paymentLogAmount = paymentLogAmount;
        this.payment = payment;
    }
}
