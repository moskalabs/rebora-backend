package moska.rebora.Theater.Repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Entity.QMovie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Theater.Dto.QTheaterPageDto;
import moska.rebora.Theater.Dto.TheaterPageDto;
import moska.rebora.User.Entity.QUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QBrandMovie.brandMovie;
import static moska.rebora.Cinema.Entity.QCinema.cinema;
import static moska.rebora.Cinema.Entity.QMovieCinema.movieCinema;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;

@Slf4j
public class TheaterCustomImpl implements TheaterCustom {

    private final JPAQueryFactory queryFactory;

    public TheaterCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    MovieRepository movieRepository;

    static final LocalDateTime minAvailDate = LocalDateTime.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()));

//    @Override
//    public List<String> getAvailableDate(String theaterRegion,
//                                         LocalDateTime startMonthDate,
//                                         LocalDateTime endMonthDate,
//                                         Long movieId) {
//
//        return queryFactory.select(formattedDate).distinct()
//                .from(theater)
//                .where(
//                        theater.theaterStartDatetime.goe(startMonthDate),
//                        theater.theaterStartDatetime.goe(minAvailDate),
//                        theater.theaterEndDatetime.loe(endMonthDate),
//                        theater.theaterRegion.eq(theaterRegion),
//                        theater.recruitment.isNull(),
//                        theater.theaterCinemaBrandName.in(JPAExpressions.select(brand.brandName)
//                                .from(brandMovie)
//                                .join(brandMovie.movie, movie)
//                                .join(brandMovie.brand, brand)
//                                .where(movie.id.eq(movieId))
//                        )
//                )
//                .fetch();
//    }

    @Override
    public List<String> getAvailableDate(String theaterRegion,
                                         LocalDateTime startMonthDate,
                                         LocalDateTime endMonthDate,
                                         Long movieId) {

        return queryFactory.select(formattedDate).distinct()
                .from(movieCinema)
                .join(movieCinema.cinema, cinema)
                .join(movieCinema.movie, movie)
                .join(cinema.theaterList, theater)
                .where(
                        theater.theaterStartDatetime.goe(startMonthDate),
                        theater.theaterStartDatetime.goe(minAvailDate),
                        theater.theaterEndDatetime.loe(endMonthDate),
                        cinema.regionName.eq(theaterRegion),
                        theater.recruitment.isNull(),
                        movie.id.eq(movieId)
                )
                .fetch();
    }

    @Override
    @Transactional
    public Page<TheaterPageDto> getPageTheater(String theaterRegion,
                                               LocalDate selectDate,
                                               Long movieId,
                                               Pageable pageable
    ) {

        LocalDateTime startDate = selectDate.atStartOfDay();
        LocalDateTime endDate = selectDate.atTime(LocalTime.MAX);

        QMovie movieEx = new QMovie("movie");

        List<TheaterPageDto> content = queryFactory.select(new QTheaterPageDto(
                        theater.id.as("theaterId"),
                        theater.theaterName,
                        theater.theaterStartDatetime.as("theaterStartTime"),
                        theater.theaterEndDatetime.as("theaterEndTime"),
                        theater.theaterDay,
                        theater.theaterRegion,
                        ExpressionUtils.as(select(movieEx.moviePrice.add(theater.theaterPrice))
                                .from(movieEx)
                                .where(movieEx.id.eq(movieId)) , "theaterPrice"),
                        theater.theaterCinemaBrandName,
                        theater.theaterCinemaName,
                        theater.theaterMaxPeople,
                        theater.theaterMinPeople,
                        theater.theaterTime,
                        recruitment.id.as("recruitmentId"),
                        recruitment.recruitmentStatus
                ))
                .from(movieCinema)
                .join(movieCinema.cinema, cinema)
                .join(movieCinema.movie, movie)
                .join(cinema.theaterList, theater)
                .leftJoin(theater.recruitment, recruitment)
                .where(
                        movie.id.eq(movieId),
                        theater.theaterStartDatetime.goe(startDate),
                        theater.theaterEndDatetime.loe(endDate),
                        theater.theaterRegion.eq(theaterRegion),
                        theater.recruitment.isNull().or(theater.recruitment.recruitmentStatus.eq(RecruitmentStatus.RECRUITING)).or(theater.recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION))
                )
                .orderBy(theater.recruitment.id.asc().nullsFirst())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(theater.id.count())
                .from(movieCinema)
                .join(movieCinema.cinema, cinema)
                .join(movieCinema.movie, movie)
                .join(cinema.theaterList, theater)
                .leftJoin(theater.recruitment, recruitment)
                .where(
                        movie.id.eq(movieId),
                        theater.theaterStartDatetime.goe(startDate),
                        theater.theaterEndDatetime.loe(endDate),
                        theater.theaterRegion.eq(theaterRegion),
                        theater.recruitment.isNull().or(theater.recruitment.recruitmentStatus.eq(RecruitmentStatus.RECRUITING)).or(theater.recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION))
                );

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    StringTemplate formattedDate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            theater.theaterStartDatetime,
            ConstantImpl.create("%d")
    );
}
