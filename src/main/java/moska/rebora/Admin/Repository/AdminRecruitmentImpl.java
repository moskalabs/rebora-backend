package moska.rebora.Admin.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QBrandMovie.brandMovie;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;

public class AdminRecruitmentImpl implements AdminRecruitment {

    private final JPAQueryFactory queryFactory;

    public AdminRecruitmentImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RecruitmentInfoDto> getRecruitmentPage(Pageable pageable, UserSearchCondition searchCondition) {
        List<RecruitmentInfoDto> content = queryFactory.select(Projections.fields(
                        RecruitmentInfoDto.class,
                        recruitment.id.as("recruitmentId"),
                        recruitment.recruitmentEndDate,
                        recruitment.recruitmentPeople,
                        recruitment.recruitmentStatus,
                        movie.movieName,
                        theater.theaterRegion,
                        theater.theaterName,
                        theater.theaterCinemaName,
                        theater.theaterStartDatetime,
                        theater.theaterMinPeople,
                        theater.theaterMaxPeople
                ))
                .from(recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .where(getSearchWord(searchCondition.getSearchWord(), searchCondition.getSearchCondition()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(recruitment.count())
                .from(recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .where(getSearchWord(searchCondition.getSearchWord(), searchCondition.getSearchCondition()))
                .fetchFirst();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public RecruitmentInfoDto getAdminRecruitmentInfo(Long recruitmentId) {
        return queryFactory.select(Projections.fields(
                        RecruitmentInfoDto.class,
                        movie.id.as("movieId"),
                        movie.movieName.as("movieName"),
                        movie.movieRunningTime,
                        theater.id.as("theaterId"),
                        theater.theaterStartDatetime,
                        theater.theaterEndDatetime,
                        theater.theaterDay,
                        theater.theaterMaxPeople,
                        theater.theaterMinPeople,
                        theater.theaterName,
                        theater.theaterCinemaName,
                        theater.theaterCinemaBrandName,
                        theater.theaterRegion,
                        recruitment.id.as("recruitmentId"),
                        recruitment.recruitmentEndDate,
                        recruitment.recruitmentStatus,
                        recruitment.recruitmentPeople,
                        recruitment.recruitmentIntroduce,
                        recruitment.recruitmentExposeYn,
                        ExpressionUtils.as(select(user.userNickname)
                                .from(user)
                                .where(user.userEmail.eq(recruitment.createdBy)), "recruiterNickname")
                ))
                .from(recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .fetchFirst();
    }

    public BooleanExpression getSearchWord(String searchWord, String searchCondition) {
        if (hasText(searchWord)) {
            if (searchCondition.equals("movieName")) {
                return movie.movieName.contains(searchWord);
            } else {
                return theater.theaterRegion.eq(searchWord);
            }
        } else {
            return null;
        }
    }
}
