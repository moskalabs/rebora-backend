package moska.rebora.User.Repository;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.QRecruitment;
import moska.rebora.User.DTO.UserImageListDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.QUser;
import moska.rebora.User.Entity.QUserRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Movie.Entity.QMovie.movie;
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
                .where(user.userEmail.eq(userEmail))
                .fetchOne();
    }

    @Override
    public Long countMyRecruiter(@Param("userEmail") @Valid String userEmail) {
        return queryFactory.select(recruitment.id.count())
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
                                ExpressionUtils.as(select(recruiterUser.userImage.as("recruitmentUserImage"))
                                        .from(recruiterUser)
                                        .where(recruiterUser.userEmail.eq(recruitment.createdBy)), "recruitmentUserImage"),
                                recruitment.recruitmentUserImages
                        ))
                .from(userRecruitment)
                .leftJoin(userRecruitment.user, user)
                .leftJoin(userRecruitment.recruitment, recruitment)
                .leftJoin(recruitment.movie, movie)
                .leftJoin(recruitment.theater, theater)
                .where(user.userUseYn.eq(true),
                        recruitment.recruitmentExposeYn.eq(true),
                        userEmailEq(userEmail),
                        createByMe(userEmail, userSearchCondition.isCreateByMe())
                )
                .orderBy(
                        recruitment.regDate.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        content.forEach(m -> {
                    if (m.getRecruitmentUserImages() != null) {
                        m.addUserImage(getUserImageList(m.getRecruitmentUserImages()));
                    }
                }
        );

        JPAQuery<Long> total = queryFactory.select(userRecruitment.id.count())
                .from(userRecruitment)
                .innerJoin(userRecruitment.user, user)
                .innerJoin(userRecruitment.recruitment, recruitment)
                .where(user.userUseYn.eq(true),
                        recruitment.recruitmentExposeYn.eq(true),
                        userEmailEq(userEmail),
                        createByMe(userEmail, userSearchCondition.isCreateByMe())
                );


        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    public List<UserImageListDto> getUserImageList(String recruitmentUserImages) {
        List<String> splitList = List.of(recruitmentUserImages.split("\\|"));
        if (splitList.isEmpty()) {
            return null;
        } else {
            List<UserImageListDto> userImageListDtoList = splitList.stream().map(UserImageListDto::new).collect(Collectors.toList());
            return userImageListDtoList;
        }
    }

    private BooleanExpression userEmailEq(String userEmail) {
        return hasText(userEmail) ? user.userEmail.eq(userEmail) : null;
    }

    private BooleanExpression createByMe(String userEmail, Boolean createByMe) {
        return hasText(userEmail) && createByMe != null && createByMe ? recruitment.createdBy.eq(userEmail) : null;
    }
}
