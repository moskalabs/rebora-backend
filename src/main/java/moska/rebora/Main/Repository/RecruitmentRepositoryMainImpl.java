package moska.rebora.Main.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.QRecruitment;
import moska.rebora.User.DTO.UserImageListDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;

import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

import moska.rebora.User.Entity.QUser;
import moska.rebora.User.Entity.QUserRecruitment;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;

public class RecruitmentRepositoryMainImpl implements RecruitmentRepositoryMain {

    private final JPAQueryFactory queryFactory;

    public RecruitmentRepositoryMainImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<UserRecruitmentListDto> getRecruitmentMainList(@Param("userEmail") String userEmail) {

        QUser recruiterUser = new QUser("recruiterUser");

        List<UserRecruitmentListDto> content = queryFactory.select(
                        Projections.fields(
                                UserRecruitmentListDto.class,
                                userRecruitment.id.as("userRecruitmentId"),
                                userRecruitment.userRecruitmentWish,
                                userRecruitment.userRecruitmentYn,
                                userRecruitment.userRecruitmentPeople,
                                movie.movieName,
                                movie.movieImage,
                                movie.id.as("movieId"),
                                theater.id.as("theaterId"),
                                theater.theaterStartDatetime,
                                theater.theaterEndDatetime,
                                theater.theaterDay,
                                theater.theaterMaxPeople,
                                theater.theaterMinPeople,
                                theater.theaterCinemaName,
                                theater.theaterCinemaBrandName,
                                theater.theaterRegion,
                                recruitment.id.as("recruitmentId"),
                                recruitment.recruitmentEndDate,
                                recruitment.recruitmentStatus,
                                recruitment.createdBy.as("recruitmentUsername"),
                                recruitment.recruitmentPeople,
                                ExpressionUtils.as(select(user.userImage.as("recruiterUserImage"))
                                        .from(user)
                                        .where(user.userEmail.eq(recruitment.createdBy)), "recruiterUserImage"),
                                ExpressionUtils.as(select(recruiterUser.userNickname.as("recruiterNickname"))
                                        .from(recruiterUser)
                                        .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterNickname")
                        ))
                .from(recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .leftJoin(recruitment.userRecruitmentList, userRecruitment).on(userRecruitment.user.userEmail.eq(userEmail))
                .where(recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION))
                .orderBy(theater.theaterStartDatetime.asc())
                .offset(0)
                .limit(10)
                .fetch();

        content.forEach(m -> m.addUserImage(getUserImageList(m.getRecruitmentId())));

        return content;
    }

    public List<UserImageListDto> getUserImageList(Long recruitmentId) {

        return queryFactory.select(Projections.fields(
                        UserImageListDto.class,
                        user.userImage,
                        user.userNickname
                )).from(userRecruitment)
                .join(userRecruitment.user, user)
                .join(userRecruitment.recruitment, recruitment)
                .where(recruitment.id.eq(recruitmentId))
                .offset(0)
                .limit(5)
                .fetch();
    }
}
