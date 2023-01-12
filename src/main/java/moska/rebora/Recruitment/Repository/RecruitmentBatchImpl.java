package moska.rebora.Recruitment.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserSearchCondition;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Payment.Entity.QPayment.payment;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

@Slf4j
public class RecruitmentBatchImpl implements RecruitmentBatch {

    private final JPAQueryFactory queryFactory;

    public RecruitmentBatchImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Recruitment> getBatchRecruitmentList(RecruitmentStatus recruitmentStatus, UserSearchCondition condition) {
        return queryFactory.select(recruitment)
                .from(recruitment)
                .join(recruitment.theater, theater).fetchJoin()
                .join(recruitment.movie).fetchJoin()
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        recruitment.recruitmentStatus.eq(recruitmentStatus),
                        theaterMinPeopleLoeGoe(condition.getRecruitmentStatus()),
                        recruitmentToday()
                )
                .fetch();
    }


    @Override
    public List<Recruitment> getBatchWaitRecruitmentList() {

        LocalDateTime baseTime = LocalDateTime.now().minusMinutes(15L);


        return queryFactory.select(recruitment)
                .from(recruitment)
                .join(recruitment.theater, theater).fetchJoin()
                .join(recruitment.movie, movie).fetchJoin()
                .where(
                        recruitment.recruitmentStatus.eq(RecruitmentStatus.WAIT),
                        recruitment.regDate.lt(baseTime)
                )
                .fetch();
    }

    @Override
    public List<Recruitment> getBatchFinishMovie() {

        return queryFactory.select(recruitment)
                .from(recruitment)
                .join(recruitment.theater, theater)
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION),
                        getFinishMovie(LocalDateTime.now())
                )
                .fetch();
    }

    BooleanExpression theaterMinPeopleLoeGoe(RecruitmentStatus recruitmentStatus) {
        if (recruitmentStatus == null) {
            return null;
        } else if (recruitmentStatus.equals(RecruitmentStatus.CONFIRMATION)) {
            return recruitment.recruitmentPeople.goe(theater.theaterMinPeople);
        } else {
            return recruitment.recruitmentPeople.lt(theater.theaterMinPeople);
        }
    }

    BooleanExpression recruitmentToday() {
        LocalDateTime startTime = LocalDate.now().atStartOfDay();
        LocalDateTime endTime = LocalDate.now().atTime(LocalTime.MAX);
        log.info("startTime={}", startTime);
        log.info("endTime={}", endTime);
        return recruitment.recruitmentEndDate.between(startTime, endTime);
    }

    BooleanExpression getFinishMovie(LocalDateTime nowTime) {
        return nowTime == null ? null : theater.theaterEndDatetime.loe(nowTime);
    }
}
