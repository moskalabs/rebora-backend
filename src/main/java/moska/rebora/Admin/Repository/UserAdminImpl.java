package moska.rebora.Admin.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Admin.Dto.AdminUserDto;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;
import static org.springframework.util.StringUtils.hasText;

public class UserAdminImpl implements UserAdmin {

    private final JPAQueryFactory queryFactory;

    public UserAdminImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminUserDto> getAdminUserPage(
            UserSearchCondition userSearchCondition,
            String startDate,
            String endDate,
            UserGrade userGrade,
            String userSnsKind,
            Pageable pageable
    ) {
        List<AdminUserDto> content = queryFactory.select(Projections.fields(
                        AdminUserDto.class,
                        user.id.as("userId"),
                        user.userEmail,
                        user.userName,
                        user.userNickname,
                        user.userUseYn,
                        user.userGrade,
                        user.userSnsKind,
                        user.regDate
                ))
                .from(user)
                .where(
                        getSearchWord(userSearchCondition.getSearchWord(), userSearchCondition.getSearchCondition()),
                        selectUserGrade(userGrade),
                        selectUserSnsKind(userSnsKind),
                        betweenDate(startDate, endDate)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(
                        user.count())
                .from(user)
                .where(
                        getSearchWord(userSearchCondition.getSearchWord(), userSearchCondition.getSearchCondition()),
                        selectUserGrade(userGrade),
                        selectUserSnsKind(userSnsKind),
                        betweenDate(startDate, endDate)
                );

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    @Override
    public AdminUserDto getAdminUserInfo(Long userId) {
        AdminUserDto adminUserDto = queryFactory.select(
                Projections.fields(
                        AdminUserDto.class,
                        user.id.as("userId"),
                        user.userEmail,
                        user.userName,
                        user.userNickname,
                        user.userImage,
                        user.userUseYn,
                        user.userPushYn,
                        user.userPushNightYn,
                        user.userGrade,
                        user.userSnsKind,
                        user.regDate
                ))
                .from(user)
                .where(user.id.eq(userId))
                .fetchFirst();

        adminUserDto.setParticipationHistoryCount(countParticipationHistory(userId));
        adminUserDto.setRecruiterCount(countMyRecruiter(adminUserDto.getUserEmail()));

        return adminUserDto;
    }

    public Long countParticipationHistory(@Param("userId") @Valid Long userId) {
        return queryFactory.select(userRecruitment.id.count())
                .from(userRecruitment)
                .leftJoin(userRecruitment.user, user)
                .leftJoin(userRecruitment.recruitment, recruitment)
                .where(
                        user.id.eq(userId),
                        userRecruitment.userRecruitmentYn.eq(true)
                )
                .fetchFirst();
    }

    public Long countMyRecruiter(@Param("userEmail") @Valid String userEmail) {
        return queryFactory.select(recruitment.id.count())
                .from(recruitment)
                .where(recruitment.createdBy.eq(userEmail))
                .fetchFirst();
    }


    public BooleanExpression betweenDate(String startDate, String endDate) {
        if (startDate.equals("") || endDate.equals("")) {
            return null;
        } else {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
            return user.regDate.between(startDateTime, endDateTime);
        }
    }

    public BooleanExpression selectUserGrade(UserGrade userGrade) {
        return userGrade != null ? user.userGrade.eq(userGrade) : null;
    }

    public BooleanExpression selectUserSnsKind(String userSnsKind) {
        switch (userSnsKind) {
            case "NORMAL":
                return user.userSnsKind.isNull();
            case "NAVER":
                return user.userSnsKind.eq(UserSnsKind.NAVER);
            case "KAKAO":
                return user.userSnsKind.eq(UserSnsKind.KAKAO);
            case "APPLE":
                return user.userSnsKind.eq(UserSnsKind.APPLE);
            default:
                return null;
        }
    }

    public BooleanExpression getSearchWord(String searchWord, String searchCondition) {
        if (hasText(searchWord)) {
            if (searchCondition.equals("userEmail")) {
                return user.userEmail.contains(searchWord);
            } else if (searchCondition.equals("userName")) {
                return user.userName.contains(searchWord);
            } else {
                return user.userNickname.contains(searchWord);
            }
        } else {
            return null;
        }
    }
}
