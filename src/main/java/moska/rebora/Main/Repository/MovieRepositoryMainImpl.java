package moska.rebora.Main.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Movie.Dto.MoviePageDto;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

import static io.jsonwebtoken.lang.Strings.hasText;
import static moska.rebora.Common.CommonConst.ADULT_BIRTH;
import static moska.rebora.Common.Entity.QCategory.category;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Movie.Entity.QMovieCategory.movieCategory;
import static moska.rebora.User.Entity.QUserMovie.userMovie;

@Slf4j
public class MovieRepositoryMainImpl implements MovieRepositoryMain {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryMainImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MoviePageDto> getMovieMainList(String userEmail, String userBirth) {

        log.info("hasTest={} userEmail={} userBirth={}", hasText(userEmail), userEmail, userBirth);


        List<MoviePageDto> moviePageDtoList = queryFactory.select(Projections.fields(
                        MoviePageDto.class,
                        movie.id,
                        movie.movieName,
                        movie.movieRating,
                        movie.movieDirector,
                        movie.movieImage,
                        movie.movieBannerImage,
                        movie.movieDetailLink,
                        movie.movieRunningTime,
                        movie.moviePopularCount,
                        movie.movieStarRating,
                        userMovie.userMovieWish,
                        userMovie.id.as("userMovieId")
                ))
                .from(movie)
                .leftJoin(movie.userMovieList, userMovie).on(userMovie.user.userEmail.eq(userEmail))
                .where(notAdult(userEmail,userBirth))
                .orderBy(movie.moviePopularCount.desc())
                .offset(0)
                .limit(10)
                .fetch();

        moviePageDtoList.forEach(m -> {
            m.setConvertStartRation(convertStarRating(m.getMovieStarRating()));
            m.setCategoryList(getCateGory(m.getId()));
        });

        return moviePageDtoList;
    }

    public String convertStarRating(Integer movieStartRating) {
        int front = movieStartRating / 10;
        int back = movieStartRating % 10;
        return front + "." + back;
    }

    public List<Category> getCateGory(Long movieId) {
        return queryFactory.select(category)
                .from(movieCategory)
                .join(movieCategory.category, category)
                .join(movieCategory.movie, movie)
                .where(movie.id.eq(movieId))
                .fetch();
    }

    public BooleanExpression notAdult(String userEmail, String userBirth) {
        if(userBirth == null){
            return movie.movieRating.ne(MovieRating.ADULT);
        }else{
            LocalDate userBirthDate = LocalDate.parse(userBirth);
            return hasText(userEmail) && !userEmail.equals("anonymousUser") && userBirthDate.isBefore(ADULT_BIRTH) ? null : movie.movieRating.ne(MovieRating.ADULT);
        }
    }
}
