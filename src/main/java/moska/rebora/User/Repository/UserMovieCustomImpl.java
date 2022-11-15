package moska.rebora.User.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Common.Entity.QCategory.category;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Movie.Entity.QMovieCategory.movieCategory;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserMovie.userMovie;

public class UserMovieCustomImpl implements UserMovieCustom {

    private final JPAQueryFactory queryFactory;

    public UserMovieCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MoviePageDto> getUserMovieList(
            UserSearchCondition searchCondition,
            String userEmail,
            Pageable pageable) {

        List<MoviePageDto> content = queryFactory
                .select(
                        Projections.fields(
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
                .from(userMovie)
                .join(userMovie.movie, movie)
                .join(userMovie.user, user)
                .where(
                        userMovie.user.userEmail.eq(userEmail),
                        isUserMovieWish(searchCondition.getUserMovieWish())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        content.forEach(m -> {
            m.setConvertStartRation(convertStarRating(m.getMovieStarRating()));
            m.setCategoryList(getCateGory(m.getId()));
        });

        long total = queryFactory
                .select(movie.count())
                .from(userMovie)
                .where(
                        userMovie.user.userEmail.eq(userEmail),
                        isUserMovieWish(searchCondition.getUserMovieWish())
                )
                .join(userMovie.movie, movie)
                .join(userMovie.user, user)
                .fetchFirst();

        content.forEach(m -> {
            m.setConvertStartRation(convertStarRating(m.getMovieStarRating()));
            m.setCategoryList(getCateGory(m.getId()));
        });

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<User> getUserListByMovie(Long movieId) {
        return queryFactory.select(user)
                .from(userMovie)
                .join(userMovie.user, user)
                .join(userMovie.movie, movie)
                .where(
                        movie.id.eq(movieId),
                        userMovie.userMovieWish.eq(true)
                )
                .fetch();
    }

    public BooleanExpression isUserMovieWish(Boolean userMovieWish) {
        return userMovieWish != null && userMovieWish ? userMovie.userMovieWish.eq(true) : null;
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
}
