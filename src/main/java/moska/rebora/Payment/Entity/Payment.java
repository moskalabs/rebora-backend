package moska.rebora.Payment.Entity;

import jdk.jfr.Unsigned;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.User.Entity.UserRecruitment;

import javax.persistence.*;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @Unsigned
    @Column(name = "payment_id", nullable = false)
    private String id;

    @Column(nullable = false)
    private String paymentContent;

    @Column(nullable = false)
    private Integer paymentAmount;

    @Column(length = 20, nullable = false)
    private String paymentMethod;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus paymentStatus;

    @Column(length = 50)
    private String paymentCardCode;

    @Column(length = 50)
    private String pgProvider;

    private String paymentCardName;

    private LocalDateTime paidAt;

    @Column
    private String receiptUrl;

    @Column(length = 50)
    private String paymentCardNumber;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "payment", cascade = CascadeType.ALL)
    private UserRecruitment userRecruitment;

    @OneToMany(mappedBy = "payment")
    List<PaymentLog> paymentLogList = new ArrayList<>();

    public void updatePayment(String paymentContent, Integer paymentAmount, String paymentMethod, PaymentStatus paymentStatus, String paymentCardCode, String pgProvider, String paymentCardName, LocalDateTime paidAt, String receiptUrl, String paymentCardNumber, UserRecruitment userRecruitment) {
        this.paymentContent = paymentContent;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCardCode = paymentCardCode;
        this.pgProvider = pgProvider;
        this.paymentCardName = paymentCardName;
        this.paidAt = paidAt;
        this.receiptUrl = receiptUrl;
        this.paymentCardNumber = paymentCardNumber;
        this.userRecruitment = userRecruitment;
    }

    @Builder
    public Payment(String id, String paymentContent, Integer paymentAmount, String paymentMethod, PaymentStatus paymentStatus, String paymentCardCode, String pgProvider, String paymentCardName, LocalDateTime paidAt, String receiptUrl, String paymentCardNumber, UserRecruitment userRecruitment, List<PaymentLog> paymentLogList) {
        this.id = id;
        this.paymentContent = paymentContent;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCardCode = paymentCardCode;
        this.pgProvider = pgProvider;
        this.paymentCardName = paymentCardName;
        this.paidAt = paidAt;
        this.receiptUrl = receiptUrl;
        this.paymentCardNumber = paymentCardNumber;
        this.userRecruitment = userRecruitment;
        this.paymentLogList = paymentLogList;
    }
}
