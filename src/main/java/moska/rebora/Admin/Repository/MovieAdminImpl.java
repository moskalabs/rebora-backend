package moska.rebora.Admin.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QBrandMovie.brandMovie;
import static moska.rebora.Common.Entity.QCategory.category;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Movie.Entity.QMovieCategory.movieCategory;
import static org.springframework.util.StringUtils.hasText;

public class MovieAdminImpl implements MovieAdmin {
    private final JPAQueryFactory queryFactory;

    public MovieAdminImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MoviePageDto> getAdminMovieList(UserSearchCondition searchCondition, Pageable pageable) {
        List<MoviePageDto> content = queryFactory.select(Projections.fields(
                        MoviePageDto.class,
                        movie.id,
                        movie.movieName,
                        movie.movieImage,
                        movie.movieRunningTime,
                        movie.moviePopularCount
                ))
                .from(movie)
                .leftJoin(movie.cinemaBrandMovieList, brandMovie)
                .leftJoin(brandMovie.brand, brand)
                .where(getSearchWord(searchCondition.getSearchWord(), searchCondition.getSearchCondition()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(movie.movieName)
                .orderBy(movie.moviePopularCount.desc())
                .fetch();

        long total = queryFactory
                .select(movie.count())
                .from(movie)
                .leftJoin(movie.cinemaBrandMovieList, brandMovie)
                .leftJoin(brandMovie.brand, brand)
                .where(getSearchWord(searchCondition.getSearchWord(), searchCondition.getSearchCondition()))
                .groupBy(movie.movieName)
                .fetchFirst();

//        content.forEach(m -> {
//            m.setConvertStartRation(convertStarRating(m.getMovieStarRating()));
//            m.setCategoryList(getCateGory(m.getId()));
//        });

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public MoviePageDto getMovieInfo(Long movieId) {
        MoviePageDto moviePageDto = queryFactory.select(Projections.fields(
                        MoviePageDto.class,
                        movie.id,
                        movie.movieName,
                        movie.movieRating,
                        movie.movieDirector,
                        movie.movieImage,
                        movie.movieBannerImage,
                        movie.movieRecruitmentImage,
                        movie.movieDetailLink,
                        movie.movieRunningTime,
                        movie.moviePrice,
                        movie.moviePopularCount,
                        movie.movieStarRating,
                        movie.movieUseYn
                ))
                .from(movie)
                .where(movie.id.eq(movieId))
                .fetchFirst();

        moviePageDto.setConvertStartRation(convertStarRating(moviePageDto.getMovieStarRating()));
        moviePageDto.setCategoryList(getCateGory(moviePageDto.getId()));

        return moviePageDto;
    }

    public BooleanExpression getSearchWord(String searchWord, String searchCondition) {
        if (hasText(searchWord)) {
            if (searchCondition.equals("movieName")) {
                return movie.movieName.contains(searchWord);
            } else {
                return brand.brandName.eq(searchWord);
            }
        } else {
            return null;
        }
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
