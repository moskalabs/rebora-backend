package moska.rebora.Admin.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.AdminRegionDto;
import moska.rebora.Admin.Dto.AdminTheaterDto;
import moska.rebora.Admin.Dto.QAdminTheaterDto;
import moska.rebora.Enum.RecruitmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;

@Slf4j
public class TheaterAdminImpl implements TheaterAdmin {

    private final JPAQueryFactory queryFactory;

    public TheaterAdminImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<AdminTheaterDto> getAdminPage(String theaterRegion, String theaterCinemaBrandName, LocalDate selectDate, Pageable pageable) {

        log.info("selectDate={}", selectDate);

        LocalDateTime startMonthDate = selectDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endMonthDate = selectDate.withDayOfMonth(selectDate.lengthOfMonth()).atTime(LocalTime.MAX);

        List<AdminTheaterDto> content = queryFactory.select(new QAdminTheaterDto(
                        theater.id.as("theaterId"),
                        theater.theaterName,
                        theater.theaterStartDatetime.as("theaterStartTime"),
                        theater.theaterEndDatetime.as("theaterEndTime"),
                        theater.theaterDay,
                        theater.theaterRegion,
                        theater.theaterPrice,
                        theater.theaterCinemaBrandName,
                        theater.theaterCinemaName,
                        theater.theaterMaxPeople,
                        theater.theaterMinPeople,
                        theater.theaterTime,
                        recruitment.id.as("recruitmentId"),
                        recruitment.recruitmentStatus,
                        recruitment.recruitmentPeople
                ))
                .from(theater)
                .leftJoin(theater.recruitment, recruitment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(recruitmentStatusOrderBy())
                .where(
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.eq(theaterCinemaBrandName),
                        theater.theaterStartDatetime.between(startMonthDate, endMonthDate)
                )
                .fetch();

        JPAQuery<Long> total = queryFactory.select(
                        theater.count())
                .from(theater)
                .where(
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.eq(theaterCinemaBrandName),
                        theater.theaterStartDatetime.between(startMonthDate, endMonthDate)
                );

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    @Override
    public AdminTheaterDto getAdminDetail(Long theaterId) {
        return queryFactory.select(
                        new QAdminTheaterDto(
                                theater.id.as("theaterId"),
                                theater.theaterName,
                                theater.theaterStartDatetime.as("theaterStartTime"),
                                theater.theaterEndDatetime.as("theaterEndTime"),
                                theater.theaterDay,
                                theater.theaterRegion,
                                theater.theaterPrice,
                                theater.theaterCinemaBrandName,
                                theater.theaterCinemaName,
                                theater.theaterMaxPeople,
                                theater.theaterMinPeople,
                                theater.theaterTime,
                                recruitment.id.as("recruitmentId"),
                                recruitment.recruitmentStatus,
                                recruitment.recruitmentPeople
                        ))
                .from(theater)
                .leftJoin(theater.recruitment, recruitment)
                .where(theater.id.eq(theaterId))
                .fetchFirst();
    }

    @Override
    public List<AdminRegionDto> getAdminRegion() {
        return queryFactory.select(Projections.fields(AdminRegionDto.class,
                        theater.theaterRegion.as("region"),
                        theater.id.count().as("regionCount")
                ))
                .from(theater)
                .groupBy(theater.theaterRegion)
                .fetch();
    }

    private OrderSpecifier<Integer> recruitmentStatusOrderBy() {
        NumberExpression<Integer> cases = new CaseBuilder()
                .when(recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION)).then(1)
                .when(recruitment.recruitmentStatus.eq(RecruitmentStatus.RECRUITING)).then(2)
                .when(recruitment.recruitmentStatus.eq(RecruitmentStatus.COMPLETED)).then(3)
                .when(recruitment.recruitmentStatus.eq(RecruitmentStatus.CANCEL)).then(4)
                .otherwise(5);
        return new OrderSpecifier<>(Order.ASC, cases);
    }

    public Long checkTheaterCsv(
            String theaterRegion,
            String theaterCinemaBrandName,
            String theaterCinemaName,
            String theaterName,
            LocalDateTime theaterStartDatetime,
            LocalDateTime theaterEndDatetime
    ) {
        return queryFactory.select(theater.count())
                .from(theater)
                .where(
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.eq(theaterCinemaBrandName),
                        theater.theaterCinemaName.eq(theaterCinemaName),
                        theater.theaterName.eq(theaterName),
                        theater.theaterStartDatetime.between(theaterStartDatetime, theaterEndDatetime)
                                .or(theater.theaterEndDatetime.between(theaterStartDatetime, theaterEndDatetime))
                                .or(theater.theaterStartDatetime.loe(theaterStartDatetime).and(theater.theaterEndDatetime.goe(theaterEndDatetime)))
                )
                .fetchOne();
    }
}
