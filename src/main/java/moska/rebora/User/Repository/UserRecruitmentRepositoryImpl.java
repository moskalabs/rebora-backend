package moska.rebora.User.Repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Enum.RecruitmentStatus;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import java.util.List;

import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

public class UserRecruitmentRepositoryImpl implements UserRecruitmentCustom {


    private final JPAQueryFactory queryFactory;

    public UserRecruitmentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Long countParticipationHistory(@Param("userEmail") @Valid String userEmail) {
        return queryFactory.select(userRecruitment.id.count())
                .from(userRecruitment)
                .leftJoin(userRecruitment.user, user)
                .leftJoin(userRecruitment.recruitment , recruitment)
                .where(user.userEmail.eq(userEmail))
                .where(recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION))
                .fetchOne();
    }

    @Override
    public Long countMyRecruiter(@Param("userEmail") @Valid String userEmail){
        return queryFactory.select(userRecruitment.id.count() )
                .from(userRecruitment)
                .leftJoin(userRecruitment.recruitment, recruitment)
                .where(recruitment.createdBy.eq(userEmail))
                .fetchOne();
    }
}
