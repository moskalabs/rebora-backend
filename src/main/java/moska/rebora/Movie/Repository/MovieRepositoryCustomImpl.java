package moska.rebora.Movie.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static moska.rebora.Common.Entity.QCategory.category;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Movie.Entity.QMovieCategory.movieCategory;
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
        List<OrderSpecifier> ORDERS = getListOrders(pageable);

        List<MoviePageDto> content = queryFactory
                .select(Projections.fields(
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
                )).distinct()
                .from(movie)
                .leftJoin(movie.userMovieList, userMovie).on(userMovie.user.userEmail.eq(userEmail))
                .leftJoin(movie.movieCategoryList, movieCategory)
                .leftJoin(movieCategory.category, category)
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
                .join(movie.movieCategoryList, movieCategory)
                .join(movieCategory.category, category)
                .fetchFirst();

        content.forEach(m -> {
            m.setConvertStartRation(convertStarRating(m.getMovieStarRating()));
            m.setCategoryList(getCateGory(m.getId()));
        });

        return new PageImpl<>(content, pageable, total);
    }


    public BooleanExpression getCategory(String selectCategory) {
        return hasText(selectCategory) && !selectCategory.equals("all") ? category.categoryName.eq(selectCategory) : null;
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

    public List<Category> getCateGory(Long movieId) {
        return queryFactory.select(category)
                .from(movieCategory)
                .join(movieCategory.category, category)
                .join(movieCategory.movie, movie)
                .where(movie.id.eq(movieId))
                .fetch();
    }
}
