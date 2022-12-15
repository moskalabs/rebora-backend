package moska.rebora.Payment.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    public List<Payment> getBatchPaymetList(PaymentStatus paymentStatus) {

        LocalDateTime beforeDate = LocalDate.now().minusDays(3).atStartOfDay();
        LocalDateTime now = LocalDate.now().minusDays(1).atTime(LocalTime.MAX).minusMinutes(30L);

        return queryFactory
                .select(payment)
                .from(payment)
                .join(payment.userRecruitment, userRecruitment).fetchJoin()
                .join(userRecruitment.recruitment, recruitment).fetchJoin()
                .join(recruitment.movie, movie).fetchJoin()
                .join(recruitment.theater, theater).fetchJoin()
                .join(userRecruitment.user, user).fetchJoin()
                .where(
                        payment.paymentStatus.eq(paymentStatus),
                        recruitment.recruitmentEndDate.between(beforeDate, now)
                )
                .fetch();
    }
}
