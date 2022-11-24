package moska.rebora.Recruitment.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserSearchCondition;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;

public class RecruitmentBatchImpl implements RecruitmentBatch {

    private final JPAQueryFactory queryFactory;

    public RecruitmentBatchImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Recruitment> getBatchRecruitmentList(RecruitmentStatus recruitmentStatus, UserSearchCondition condition) {
        return queryFactory.select(recruitment)
                .from(recruitment)
                .join(recruitment.theater, theater)
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        recruitment.recruitmentStatus.eq(recruitmentStatus),
                        theaterMinPeopleLoeGoe(condition.getRecruitmentStatus()),
                        recruitmentToday(),
                        getFinishMovie(condition.getFinishTime())
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

    BooleanExpression recruitmentToday(){
        LocalDateTime startTime = LocalDate.now().atStartOfDay();
        LocalDateTime endTime = LocalDate.now().atTime(LocalTime.MAX);
        return recruitment.recruitmentEndDate.between(startTime, endTime);
    }

    BooleanExpression getFinishMovie(LocalDateTime nowTime){
        return nowTime == null ? null : theater.theaterEndDatetime.loe(nowTime);
    }
}
