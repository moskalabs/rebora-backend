package moska.rebora.Recruitment.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;

public class RecruitmentBatchImpl implements RecruitmentBatch {

    private final JPAQueryFactory queryFactory;

    public RecruitmentBatchImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Recruitment> getBatchRecruitmentList(RecruitmentStatus recruitmentStatus) {
        return queryFactory.select(recruitment)
                .from(recruitment)
                .join(recruitment.theater, theater)
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        recruitment.recruitmentStatus.eq(recruitmentStatus),
                        recruitment.recruitmentPeople.goe(theater.theaterMinPeople)
                )
                .fetch();
    }
}
