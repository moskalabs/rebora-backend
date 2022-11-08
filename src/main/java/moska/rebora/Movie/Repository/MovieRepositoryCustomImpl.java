package moska.rebora.Movie.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Movie.Dto.MovieDto;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.User.Entity.QUserMovie.userMovie;
import static org.springframework.util.StringUtils.hasText;

public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MoviePageDto> getMovieList(@Param("searchCondition") UserSearchCondition searchCondition,
                                           @Param("userEmail") String userEmail,
                                           @Param("pageable") Pageable pageable
    ) {
        List<MoviePageDto> content = queryFactory
                .select(Projections.fields(
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
                .leftJoin(movie.userMovieList, userMovie).on(userMovie.user.userEmail.eq(userEmail))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(getCategory(searchCondition.getCategory()))
                .orderBy(getListOrders(searchCondition.getOrderByMovie()))
                .fetch();

        long total = queryFactory.select(movie.count()).from(movie).fetchFirst();

        return new PageImpl<>(content, pageable, total);
    }

    public BooleanExpression getCategory(String category) {
        return hasText(category) && !category.equals("all") ? movie.movieCategory.contains(category) : null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public OrderSpecifier<?> getListOrders(String orderByMovie) {

        if (orderByMovie.equals("popular")) {
            return new OrderSpecifier(Order.DESC, movie.moviePopularCount);
        } else if (orderByMovie.equals("name")) {
            return new OrderSpecifier(Order.ASC, movie.movieName);
        } else {
            return new OrderSpecifier(Order.DESC, movie.movieStarRating);
        }
    }
}
