package moska.rebora.User.Repository;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.QUserImageListDto;
import moska.rebora.User.DTO.UserImageListDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.QUser;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Payment.Entity.QPayment.payment;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
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
                .leftJoin(userRecruitment.recruitment, recruitment)
                .where(
                        user.userEmail.eq(userEmail),
                        userRecruitment.userRecruitmentYn.eq(true)
                )
                .fetchOne();
    }

    @Override
    public Long countMyRecruiter(@Param("userEmail") @Valid String userEmail) {
        return queryFactory.select(
                        recruitment.id.count()
                )
                .from(recruitment)
                .where(recruitment.createdBy.eq(userEmail))
                .fetchOne();
    }

    @Override
    public Page<UserRecruitmentListDto> getUserRecruitmentList(@Param("userEmail") String userEmail,
                                                               Pageable pageable,
                                                               UserSearchCondition userSearchCondition) {

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
                                ExpressionUtils.as(select(recruiterUser.userImage.as("recruiterUserImage"))
                                        .from(recruiterUser)
                                        .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterUserImage"),
                                ExpressionUtils.as(select(recruiterUser.userNickname.as("recruiterNickname"))
                                        .from(recruiterUser)
                                        .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruiterNickname"),
                                payment.paymentAmount,
                                payment.paymentStatus,
                                payment.paidAt,
                                payment.paymentMethod,
                                payment.id.as("paymentId")
                        ))
                .from(userRecruitment)
                .join(userRecruitment.user, user)
                .leftJoin(userRecruitment.recruitment, recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .leftJoin(userRecruitment.payment, payment)
                .where(
                        user.userUseYn.eq(true),
                        recruitment.recruitmentExposeYn.eq(true),
                        isUserRecruitmentYn(userSearchCondition.getUserRecruitmentYn()),
                        userEmailEq(userEmail),
                        createByMe(userEmail, userSearchCondition.isCreateByMe()),
                        isWishes(userSearchCondition.getUserRecruitmentWish())
                )
                .orderBy(
                        recruitment.regDate.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        content.forEach(m -> m.addUserImage(getUserImageList(m.getRecruitmentId())));

        JPAQuery<Long> total = queryFactory.select(userRecruitment.id.count())
                .from(userRecruitment)
                .leftJoin(userRecruitment.user, user)
                .leftJoin(userRecruitment.recruitment, recruitment)
                .where(user.userUseYn.eq(true),
                        recruitment.recruitmentExposeYn.eq(true),
                        isUserRecruitmentYn(userSearchCondition.getUserRecruitmentYn()),
                        userEmailEq(userEmail),
                        createByMe(userEmail, userSearchCondition.isCreateByMe()),
                        isWishes(userSearchCondition.getUserRecruitmentWish())
                );


        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    /**
     * 모집별 이미지 가져오기
     *
     * @param userEmail     유저 이메일
     * @param recruitmentId 모집 아이디
     * @return List<UserImageListDto>
     */
    @Override
    public List<UserImageListDto> getUserImageListByRecruitment(String userEmail, Long recruitmentId) {
        return queryFactory.select(new QUserImageListDto(
                        user.userImage,
                        user.userNickname
                ))
                .from(userRecruitment)
                .join(userRecruitment.recruitment, recruitment)
                .join(userRecruitment.user, user)
                .where(
                        recruitment.id.eq(recruitmentId),
                        userRecruitment.userRecruitmentYn.eq(true)
                )
                .orderBy(userEmailOrderBy(userEmail))
                .fetch();
    }

    /**
     * 유저 이미지 변환
     *
     * @param recruitmentId 유저 썸네일
     * @return List<UserImageListDto>
     */
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

    public List<UserRecruitment> getUserRecruitmentByRecruitment(Recruitment recruitment) {
        return queryFactory.select(userRecruitment)
                .from(userRecruitment)
                .join(userRecruitment.user, user).fetchJoin()
                .where(userRecruitment.recruitment.eq(recruitment),
                        userRecruitment.userRecruitmentYn.eq(true),
                        user.userEmail.ne(recruitment.getCreatedBy())
                )
                .fetch();
    }

    @Override
    public List<UserRecruitment> getBatchRefundUserRecruitment(Long recruitmentId) {
        return queryFactory.select(userRecruitment)
                .from(userRecruitment)
                .join(userRecruitment.recruitment, recruitment).on(recruitment.id.eq(recruitmentId))
                .join(userRecruitment.payment, payment).fetchJoin()
                .join(userRecruitment.user, user).fetchJoin()
                .where(
                        payment.paymentStatus.eq(PaymentStatus.COMPLETE)
                )
                .fetch();
    }

    @Override
    public List<UserRecruitment> getBatchUserWishRecruitment(Long recruitmentId) {
        return queryFactory.select(userRecruitment)
                .from(userRecruitment)
                .join(userRecruitment.recruitment, recruitment).on(recruitment.id.eq(recruitmentId))
                .join(userRecruitment.user, user).fetchJoin()
                .where(
                        userRecruitment.userRecruitmentWish.eq(true)
                )
                .fetch();
    }

    public Optional<UserRecruitment> getUserRecruitmentById(Long userRecruitmentId) {
        return Optional.ofNullable(queryFactory.select(userRecruitment)
                .from(userRecruitment)
                .join(userRecruitment.user, user).fetchJoin()
                .join(userRecruitment.recruitment, recruitment).fetchJoin()
                .join(recruitment.movie, movie).fetchJoin()
                .join(recruitment.theater, theater).fetchJoin()
                .where(userRecruitment.id.eq(userRecruitmentId))
                .fetchOne());
    }

    //자신의 프로필 먼저
    private OrderSpecifier<Integer> userEmailOrderBy(String userEmail) {
        NumberExpression<Integer> cases = new CaseBuilder().when(user.userEmail.eq(userEmail)).then(1).otherwise(2);
        return new OrderSpecifier<>(Order.ASC, cases);
    }

    //유저 이메일 일치
    private BooleanExpression userEmailEq(String userEmail) {
        return hasText(userEmail) ? user.userEmail.eq(userEmail) : null;
    }

    private BooleanExpression isUserRecruitmentYn(Boolean userRecruitmentYn) {
        return userRecruitmentYn != null ? userRecruitment.userRecruitmentYn.eq(userRecruitmentYn) : null;
    }

    //내가 모집한 게시물
    private BooleanExpression createByMe(String userEmail, Boolean createByMe) {
        return hasText(userEmail) && createByMe != null && createByMe ? recruitment.createdBy.eq(userEmail) : null;
    }

    private BooleanExpression isWishes(Boolean userRecruitmentWish) {
        return userRecruitmentWish != null && userRecruitmentWish ? userRecruitment.userRecruitmentWish.eq(true) : null;
    }
}
