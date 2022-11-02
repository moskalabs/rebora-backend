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

    @Column(nullable = false)
    @ColumnDefault("1")
    private Integer userRecruitmentPeople;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Builder
    public UserRecruitment(Boolean userRecruitmentWish, Boolean userRecruitmentYn, Integer userRecruitmentPeople, User user, Recruitment recruitment, Payment payment) {
        this.userRecruitmentWish = userRecruitmentWish;
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
        this.user = user;
        this.recruitment = recruitment;
        this.payment = payment;
    }
}
