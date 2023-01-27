package moska.rebora.Cinema.Repository;

import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Cinema.Dto.MovieCinemaDto;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static moska.rebora.Cinema.Entity.QCinema.cinema;
import static moska.rebora.Cinema.Entity.QMovieCinema.movieCinema;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Theater.Entity.QTheater.theater;

public class MovieCinemaRepositoryCustomImpl implements MovieCinemaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MovieCinemaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    static final LocalDateTime minAvailDate = LocalDateTime.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()));

    @Override
    public List<String> getByRegionName(Long movieId) {

        return queryFactory
                .select(cinema.regionName)
                .from(movieCinema)
                .join(movieCinema.cinema, cinema)
                .join(movieCinema.movie, movie)
                .join(cinema.theaterList, theater)
                .where(
                        movie.id.eq(movieId),
                        theater.theaterTime.goe(movie.movieRunningTime),
                        theater.theaterStartDatetime.goe(minAvailDate)
                )
                .groupBy(cinema.regionName)
                .fetch();
    }

    @Override
    public List<MovieCinemaDto> getMovieCinemaByCinemaId(Long cinemaId) {
        return queryFactory.select(Projections.fields(
                        MovieCinemaDto.class,
                        movie.movieName,
                        movie.id.as("movieId")
                ))
                .from(movieCinema)
                .join(movieCinema.movie, movie)
                .join(movieCinema.cinema, cinema)
                .where(cinema.id.eq(cinemaId))
                .fetch();
    }
}
