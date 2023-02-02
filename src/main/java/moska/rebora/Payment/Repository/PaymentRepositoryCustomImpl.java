package moska.rebora.Payment.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.UserRecruitment;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Payment.Entity.QPayment.payment;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

@Slf4j
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PaymentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Payment> getBatchPaymentList(PaymentStatus paymentStatus) {

        LocalDateTime beforeDate = LocalDate.now().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        return queryFactory
                .select(payment)
                .from(payment)
                .join(payment.userRecruitment, userRecruitment).fetchJoin()
                .join(userRecruitment.recruitment, recruitment).fetchJoin()
                .join(recruitment.movie, movie).fetchJoin()
                .join(recruitment.theater, theater).fetchJoin()
                .join(userRecruitment.user, user).fetchJoin()
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION),
                        payment.paymentStatus.eq(paymentStatus),
                        payment.paymentReserve.eq(true),
                        recruitment.recruitmentEndDate.between(beforeDate, now)
                )
                .fetch();
    }

    @Override
    public Optional<Payment> getPaymentByUserRecruitment(UserRecruitment userRecruitments) {

        if(userRecruitments.getPayment() == null){
            return Optional.empty();
        }else{
            return Optional.ofNullable(queryFactory
                    .select(payment)
                    .from(payment)
                    .where(payment.id.eq(userRecruitments.getPayment().getId()))
                    .fetchOne());
        }
    }
}
