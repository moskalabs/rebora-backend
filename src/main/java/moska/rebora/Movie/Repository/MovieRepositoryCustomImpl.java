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
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.User.Entity.QUserMovie.userMovie;
import static org.apache.logging.log4j.ThreadContext.isEmpty;
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
        List<OrderSpecifier> ORDERS = getListOrders(pageable);

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
                        userMovie.userMovieWish,
                        userMovie.id.as("userMovieId")
                ))
                .from(movie)
                .leftJoin(movie.userMovieList, userMovie).on(userMovie.user.userEmail.eq(userEmail))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .where(getCategory(searchCondition.getCategory()),
                        getSearchWord(searchCondition.getSearchWord())
                )
                .orderBy(ORDERS.toArray(OrderSpecifier[]::new))
                .fetch();

        long total = queryFactory
                .select(movie.count())
                .from(movie)
                .where(getCategory(searchCondition.getCategory()),
                        getSearchWord(searchCondition.getSearchWord())
                        )
                .fetchFirst();

        content.forEach(m -> m.setConvertStartRation(convertStarRating(m.getMovieStarRating())));

        return new PageImpl<>(content, pageable, total);
    }

    public BooleanExpression getCategory(String category) {
        return hasText(category) && !category.equals("all") ? movie.movieCategory.contains(category) : null;
    }

    public BooleanExpression getSearchWord(String searchWord) {
        return hasText(searchWord) ? movie.movieName.contains(searchWord) : null;
    }

    public List<OrderSpecifier> getListOrders(Pageable pageable) {
        List<OrderSpecifier> ORDERS = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "moviePopularCount":
                    ORDERS.add(new OrderSpecifier(direction, movie.moviePopularCount));
                    break;
                case "movieName":
                    ORDERS.add(new OrderSpecifier(direction, movie.movieName));
                    break;
                case "movieStarRating":
                    ORDERS.add(new OrderSpecifier(direction, movie.movieStarRating));
                    break;
                default:
                    ORDERS.add(new OrderSpecifier(Order.DESC, movie.regDate));
                    break;
            }
        }

        return ORDERS;
    }

    public String convertStarRating(Integer movieStartRating) {
        int front = movieStartRating / 10;
        int back = movieStartRating % 10;
        return front + "." + back;
    }
}
