package moska.rebora.Main.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.User.Entity.QUserMovie.userMovie;

public class MovieRepositoryMainImpl implements MovieRepositoryMain {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryMainImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MoviePageDto> getMovieMainList(String userEmail) {
        return queryFactory.select(Projections.fields(
                        MoviePageDto.class,
                        movie.id,
                        movie.movieName,
                        movie.movieRating,
                        movie.movieCategory,
                        movie.movieDirector,
                        movie.movieImage,
                        movie.movieBannerImage,
                        movie.movieDetailLink,
                        movie.movieRunningTime,
                        movie.moviePopularCount,
                        movie.movieStarRating,
                        userMovie.userMovieWish
                ))
                .from(movie)
                .leftJoin(movie.userMovieList, userMovie).on(userMovie.user.userEmail.eq("userEmail"))
                .orderBy(movie.moviePopularCount.desc())
                .offset(0)
                .limit(10)
                .fetch();
    }
}
