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
    private String payment_key;

    @Column(nullable = false)
    private String payment_content;

    @Column(nullable = false)
    private Integer payment_amount;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentMethod payment_method;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus payment_status;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment")
    private UserRecruitment recruitment;

    @Builder
    public Payment(String payment_key, String payment_content, Integer payment_amount, PaymentMethod payment_method, PaymentStatus payment_status, UserRecruitment recruitment) {
        this.payment_key = payment_key;
        this.payment_content = payment_content;
        this.payment_amount = payment_amount;
        this.payment_method = payment_method;
        this.payment_status = payment_status;
        this.recruitment = recruitment;
    }
}
