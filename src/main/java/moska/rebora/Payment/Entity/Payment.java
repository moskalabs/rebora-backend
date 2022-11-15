package moska.rebora.Payment.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.User.Entity.UserRecruitment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "payment_id", nullable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String paymentKey;

    @Column(nullable = false)
    private String paymentContent;

    @Column(nullable = false)
    private Integer paymentAmount;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private UserRecruitment recruitment;

    @Builder
    public Payment(String paymentKey, String paymentContent, Integer paymentAmount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, UserRecruitment recruitment) {
        this.paymentKey = paymentKey;
        this.paymentContent = paymentContent;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.recruitment = recruitment;
    }
}
