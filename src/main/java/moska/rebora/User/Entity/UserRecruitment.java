package moska.rebora.User.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "user_recruitment_index", columnList = "user_id, recruitment_id")})
public class UserRecruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_recruitment_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean userRecruitmentWish;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Boolean userRecruitmentYn;

    @ColumnDefault("1")
    private Integer userRecruitmentPeople;

    @Column
    private String customerUId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;


    /**
     * 찜 변경
     *
     * @param userRecruitmentWish 찜 여부
     */
    public void changeWish(
            Boolean userRecruitmentWish
    ) {
        this.userRecruitmentWish = userRecruitmentWish;
    }

    /**
     * 모집 취소
     *
     * @param userRecruitmentYn 모집 신청 여부
     */
    public void cancelUserRecruitment(Boolean userRecruitmentYn) {
        this.userRecruitmentYn = userRecruitmentYn;
    }

    /**
     * 유저_모집 업데이트
     *
     * @param userRecruitmentYn     모집 신청 여부
     * @param userRecruitmentPeople 신청 인원
     */
    public void updateUserRecruitment(Boolean userRecruitmentYn, Integer userRecruitmentPeople) {
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
    }

    /**
     * 결제 업데이트
     *
     * @param payment 결제 엔티티
     */
    public void updatePayment(Payment payment) {
        this.payment = payment;
    }

    /**
     * 결제 빌링키 업데이트
     *
     * @param customerUId           결제 빌링키
     * @param userRecruitmentYn     모집 신청 여부
     * @param userRecruitmentPeople 모집 인원
     */
    public void applyCustomerUId(
            String customerUId,
            Boolean userRecruitmentYn,
            Integer userRecruitmentPeople
    ) {
        this.customerUId = customerUId;
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
    }

    public void updateUserRecruitmentYn(
            Boolean userRecruitmentYn
    ){
        this.userRecruitmentYn = userRecruitmentYn;
    }

    @Builder
    public UserRecruitment(Boolean userRecruitmentWish, Boolean userRecruitmentYn, Integer userRecruitmentPeople, User user, Recruitment recruitment, Payment payment, String customerUId) {
        this.userRecruitmentWish = userRecruitmentWish;
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
        this.user = user;
        this.recruitment = recruitment;
        this.payment = payment;
        this.customerUId = customerUId;
    }
}
