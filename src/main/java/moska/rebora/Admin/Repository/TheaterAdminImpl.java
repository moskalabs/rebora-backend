package moska.rebora.Admin.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Admin.Dto.AdminRegionDto;
import moska.rebora.Admin.Dto.AdminTheaterDto;
import moska.rebora.Admin.Dto.QAdminTheaterDto;
import moska.rebora.Theater.Dto.QTheaterPageDto;
import moska.rebora.Theater.Dto.TheaterPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QBrandMovie.brandMovie;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;

public class TheaterAdminImpl implements TheaterAdmin {

    private final JPAQueryFactory queryFactory;

    public TheaterAdminImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<AdminTheaterDto> getAdminPage(String theaterRegion, String theaterCinemaBrandName, LocalDate selectDate, Pageable pageable) {

        LocalDateTime startDate = selectDate.atStartOfDay();
        LocalDateTime endDate = selectDate.atTime(LocalTime.MAX);

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
                .where(
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.eq(theaterCinemaBrandName),
                        theater.theaterStartDatetime.between(startDate, endDate)
                )
                .fetch();

        JPAQuery<Long> total = queryFactory.select(
                        theater.count())
                .from(theater)
                .where(
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.eq(theaterCinemaBrandName),
                        theater.theaterStartDatetime.between(startDate, endDate)
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
}
