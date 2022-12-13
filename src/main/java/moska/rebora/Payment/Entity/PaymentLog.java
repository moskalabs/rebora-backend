package moska.rebora.Payment.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Integer paymentLogAmount;
    @Column(length = 20)
    private String paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus paymentLogStatus;

    @Column(length = 50)
    private String paymentLogCardCode;

    @Column(length = 50)
    private String pgProvider;

    private LocalDateTime paidAt;
    @Column
    private String receiptUrl;
    @Column(length = 50)
    private String paymentCardNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public void updatePaymentLogStatus(PaymentStatus paymentStatus){
        this.paymentLogStatus = paymentStatus;
    }

    @Builder
    public PaymentLog(String paymentLogContent, Integer paymentLogAmount, String paymentMethod, PaymentStatus paymentLogStatus, String paymentLogCardCode, String pgProvider, LocalDateTime paidAt, String receiptUrl, String paymentCardNumber, Payment payment) {
        this.paymentLogContent = paymentLogContent;
        this.paymentLogAmount = paymentLogAmount;
        this.paymentMethod = paymentMethod;
        this.paymentLogStatus = paymentLogStatus;
        this.paymentLogCardCode = paymentLogCardCode;
        this.pgProvider = pgProvider;
        this.paidAt = paidAt;
        this.receiptUrl = receiptUrl;
        this.paymentCardNumber = paymentCardNumber;
        this.payment = payment;
    }
}
