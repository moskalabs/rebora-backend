package moska.rebora.Admin.Repository;


import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Cinema.Dto.CinemaPageDto;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Entity.QCinema;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Cinema.Entity.QCinema.cinema;
import static moska.rebora.Cinema.Entity.QMovieCinema.movieCinema;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static org.springframework.util.StringUtils.hasText;

public class AdminCinemaRepositoryImpl implements AdminCinemaRepository {

    private final JPAQueryFactory queryFactory;

    public AdminCinemaRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CinemaPageDto> getCinemaPage(Pageable pageable, UserSearchCondition userSearchCondition) {

        QCinema theaterCinema = new QCinema("theaterCinema");

        List<CinemaPageDto> content = queryFactory.select(Projections.fields(
                        CinemaPageDto.class,
                        cinema.id.as("cinemaId"),
                        cinema.cinemaName,
                        cinema.brandName,
                        cinema.regionName,
                        ExpressionUtils.as(
                                select(theater.id.count())
                                        .from(theater)
                                        .leftJoin(theater.recruitment, recruitment)
                                        .join(theater.cinema, theaterCinema)
                                        .where(
                                                theaterCinema.id.eq(cinema.id),
                                                theater.recruitment.isNull().or(recruitment.recruitmentExposeYn.eq(true).and(recruitment.recruitmentStatus.eq(RecruitmentStatus.CONFIRMATION).and(recruitment.recruitmentStatus.eq(RecruitmentStatus.RECRUITING))))
                                        )
                                , "theaterCount"
                        )
                ))
                .from(cinema)
                .where(
                        getSearchWord(userSearchCondition.getSearchWord(), userSearchCondition.getSearchCondition()),
                        getCinemaBrand(userSearchCondition.getCinemaBrand())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(cinema.id.count())
                .from(cinema);

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    @Override
    public CinemaPageDto getCinemaInfo(Long cinemaId) {
        return queryFactory.select(Projections.fields(
                        CinemaPageDto.class,
                        cinema.id.as("cinemaId"),
                        cinema.cinemaName,
                        cinema.brandName,
                        cinema.regionName
                ))
                .from(cinema)
                .where(cinema.id.eq(cinemaId))
                .fetchOne();
    }

    public List<Cinema> getCinemaListByMovieId(Long movieId) {
        return queryFactory.select(cinema)
                .from(movieCinema)
                .join(movieCinema.movie, movie)
                .join(movieCinema.cinema, cinema)
                .where(movie.id.eq(movieId))
                .fetch();
    }

    public BooleanExpression getSearchWord(String searchWord, String searchCondition) {
        switch (searchCondition) {
            case "cinemaRegion": {
                return hasText(searchWord) ? cinema.regionName.contains(searchWord) : null;
            }
            case "cinemaName": {
                return hasText(searchWord) ? cinema.cinemaName.contains(searchWord) : null;
            }
            default: {
                return null;
            }
        }
    }

    public BooleanExpression getCinemaBrand(String cinemaBrand) {
        return hasText(cinemaBrand) ? cinema.brandName.eq(cinemaBrand) : null;
    }
}
