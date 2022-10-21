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
    private String payment_log_content;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus payment_log_status;

    @Column(nullable = false)
    private Integer payment_log_amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public PaymentLog(String payment_log_content, PaymentStatus payment_log_status, Integer payment_log_amount, Payment payment) {
        this.payment_log_content = payment_log_content;
        this.payment_log_status = payment_log_status;
        this.payment_log_amount = payment_log_amount;
        this.payment = payment;
    }
}
