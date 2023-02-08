package moska.rebora.Recruitment.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserImageListDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.QUser;
import moska.rebora.User.Entity.QUserRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
public class RecruitmentCustomImpl implements RecruitmentCustom {

    private final JPAQueryFactory queryFactory;

    public RecruitmentCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserRecruitmentListDto> getList(Pageable pageable,
                                                String userEmail,
                                                UserSearchCondition searchCondition) {

        List<OrderSpecifier> ORDERS = getListOrders(searchCondition.getRecruitmentStatus());
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
                                movie.movieDetailLink,
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
                                ExpressionUtils.as(
                                        select(recruiterUser.userImage.as("recruiterUserImage"))
                                        .from(recruiterUser)
                                        .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterUserImage"),
                                ExpressionUtils.as(select(recruiterUser.userNickname.as("recruiterNickname"))
                                        .from(recruiterUser)
                                        .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterNickname")
                        ))
                .from(recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .leftJoin(recruitment.userRecruitmentList, userRecruitment).on(userRecruitment.user.userEmail.eq(userEmail))
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        getTheaterRegion(searchCondition.getTheaterRegion()),
                        recruitmentStatus(searchCondition.getRecruitmentStatus()),
                        createSearchWord(searchCondition.getSearchWord()),
                        createMovieId(searchCondition.getMovieId())
                )
                .orderBy(
                        ORDERS.toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        content.forEach(m -> m.addUserImage(getUserImageList(m.getRecruitmentId())));

        JPAQuery<Long> total = queryFactory.select(recruitment.id.count())
                .from(recruitment)
                .leftJoin(recruitment.theater, theater)
                .leftJoin(recruitment.movie, movie)
                .where(
                        recruitment.recruitmentExposeYn.eq(true),
                        getTheaterRegion(searchCondition.getTheaterRegion()),
                        recruitmentStatus(searchCondition.getRecruitmentStatus()),
                        createSearchWord(searchCondition.getSearchWord()),
                        createMovieId(searchCondition.getMovieId())
                );


        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    @Override
    public RecruitmentInfoDto getRecruitmentInfo(Long recruitmentId, String userEmail) {

        QUser recruiterUser = new QUser("recruiterUser");

        return queryFactory.select(Projections.fields(
                        RecruitmentInfoDto.class,
                        movie.movieName,
                        movie.movieRecruitmentImage,
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
                        theater.theaterPrice.add(movie.moviePrice).as("theaterPrice"),
                        theater.theaterTime,
                        userRecruitment.id.as("userRecruitmentId"),
                        userRecruitment.userRecruitmentWish,
                        userRecruitment.userRecruitmentYn,
                        userRecruitment.userRecruitmentPeople,
                        recruitment.id.as("recruitmentId"),
                        recruitment.recruitmentEndDate,
                        recruitment.recruitmentStatus,
                        recruitment.recruitmentPeople,
                        recruitment.recruitmentIntroduce,
                        ExpressionUtils.as(select(recruiterUser.userImage.as("recruiterUserImage"))
                                .from(recruiterUser)
                                .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterUserImage"),
                        ExpressionUtils.as(select(recruiterUser.userNickname.as("recruiterNickname"))
                                .from(recruiterUser)
                                .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterNickname")
                ))
                .from(recruitment)
                .leftJoin(recruitment.theater, theater)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.userRecruitmentList, userRecruitment).on(userRecruitment.user.userEmail.eq(userEmail))
                .where(recruitment.id.eq(recruitmentId))
                .fetchOne();
    }

    @Override
    public Optional<Recruitment> getOptionalRecruitmentById(Long recruitmentId) {
        return Optional.ofNullable(
                queryFactory.select(recruitment)
                        .from(recruitment)
                        .join(recruitment.theater, theater).fetchJoin()
                        .join(recruitment.movie, movie).fetchJoin()
                        .where(recruitment.id.eq(recruitmentId))
                        .fetchOne());
    }

    private BooleanExpression getTheaterRegion(String theaterRegion) {
        return hasText(theaterRegion) ? theater.theaterRegion.eq(theaterRegion) : null;
    }

    private BooleanExpression recruitmentStatus(RecruitmentStatus recruitmentStatus) {
        return recruitmentStatus == null ? recruitment.recruitmentStatus.in(RecruitmentStatus.RECRUITING, RecruitmentStatus.CONFIRMATION)
                : recruitment.recruitmentStatus.eq(recruitmentStatus);
    }

    private BooleanExpression createSearchWord(String searchWord) {
        return hasText(searchWord) ? movie.movieName.contains(searchWord) : null;
    }

    private BooleanExpression createMovieId(Long movieId) {
        return movieId != null ? movie.id.eq(movieId) : null;
    }

    public List<OrderSpecifier> getListOrders(RecruitmentStatus recruitmentStatus) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (recruitmentStatus == null) {
            ORDERS.add(new OrderSpecifier(Order.DESC, recruitment.regDate));
        } else if (recruitmentStatus == RecruitmentStatus.RECRUITING) {
            ORDERS.add(new OrderSpecifier(Order.ASC, recruitment.recruitmentEndDate));
        } else if (recruitmentStatus == RecruitmentStatus.CONFIRMATION) {
            ORDERS.add(new OrderSpecifier(Order.ASC, theater.theaterStartDatetime));
        } else {
            ORDERS.add(new OrderSpecifier(Order.DESC, recruitment.regDate));
        }

        return ORDERS;
    }

    public List<UserImageListDto> getUserImageList(Long recruitmentId) {

        return queryFactory.select(Projections.fields(
                        UserImageListDto.class,
                        user.userImage,
                        user.userNickname
                )).from(userRecruitment)
                .join(userRecruitment.user, user)
                .join(userRecruitment.recruitment, recruitment)
                .where(
                        recruitment.id.eq(recruitmentId),
                        userRecruitment.userRecruitmentYn.eq(true)
                )
                .offset(0)
                .limit(5)
                .fetch();
    }
}
