package moska.rebora.Theater.Repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Theater.Dto.QTheaterPageDto;
import moska.rebora.Theater.Dto.TheaterPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QBrandMovie.brandMovie;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;

public class TheaterCustomImpl implements TheaterCustom {

    private final JPAQueryFactory queryFactory;

    public TheaterCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    static final LocalDateTime minAvailDate = LocalDateTime.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()));

    @Override
    public List<String> getAvailableDate(String theaterRegion,
                                         LocalDateTime startMonthDate,
                                         LocalDateTime endMonthDate,
                                         Long movieId) {

        return queryFactory.select(formattedDate).distinct()
                .from(theater)
                .where(
                        theater.theaterStartDatetime.goe(startMonthDate),
                        theater.theaterStartDatetime.goe(minAvailDate),
                        theater.theaterEndDatetime.loe(endMonthDate),
                        theater.theaterRegion.eq(theaterRegion),
                        theater.recruitment.isNull(),
                        theater.theaterCinemaBrandName.in(JPAExpressions.select(brand.brandName)
                                .from(brandMovie)
                                .join(brandMovie.movie, movie)
                                .join(brandMovie.brand, brand)
                                .where(movie.id.eq(movieId))
                        )
                )
                .fetch();
    }

    @Override
    public Page<TheaterPageDto> getPageTheater(String theaterRegion,
                                               LocalDate selectDate,
                                               Long movieId,
                                               Pageable pageable
    ) {

        LocalDateTime startDate = selectDate.atStartOfDay();
        LocalDateTime endDate = selectDate.atTime(LocalTime.MAX);

        List<TheaterPageDto> content = queryFactory.select(new QTheaterPageDto(
                        theater.id.as("theaterId"),
                        theater.theaterName,
                        theater.theaterStartDatetime.as("theaterStartTime"),
                        theater.theaterEndDatetime.as("theaterEndTime"),
                        theater.theaterDay,
                        theater.theaterCinemaBrandName,
                        theater.theaterCinemaName,
                        theater.theaterMaxPeople,
                        theater.theaterMinPeople,
                        theater.theaterTime,
                        recruitment.id.as("recruitmentId"),
                        recruitment.recruitmentStatus
                )).from(theater)
                .leftJoin(theater.recruitment, recruitment)
                .where(
                        theater.theaterStartDatetime.goe(startDate),
                        theater.theaterEndDatetime.loe(endDate),
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.in(JPAExpressions.select(brand.brandName)
                                .from(brandMovie)
                                .join(brandMovie.movie, movie)
                                .join(brandMovie.brand, brand)
                                .where(movie.id.eq(movieId))
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(theater.id.count()).from(theater).leftJoin(theater.recruitment, recruitment)
                .where(
                        theater.theaterStartDatetime.goe(startDate),
                        theater.theaterEndDatetime.loe(endDate),
                        theater.theaterRegion.eq(theaterRegion),
                        theater.theaterCinemaBrandName.in(JPAExpressions.select(brand.brandName)
                                .from(brandMovie)
                                .join(brandMovie.movie, movie)
                                .join(brandMovie.brand, brand)
                                .where(movie.id.eq(movieId))
                        )
                );

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    StringTemplate formattedDate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            theater.theaterStartDatetime,
            ConstantImpl.create("%d")
    );
}
