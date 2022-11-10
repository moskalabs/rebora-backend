package moska.rebora.Theater.Repository;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QBrandMovie.brandMovie;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Theater.Entity.QTheater.theater;

public class TheaterCustomImpl implements TheaterCustom {

    private final JPAQueryFactory queryFactory;

    public TheaterCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> getAvailableDate(String theaterRegion,
                                         LocalDateTime startMonthDate,
                                         LocalDateTime endMonthDate,
                                         Long movieId) {

        LocalDateTime minAvailDate = LocalDateTime.from(LocalDateTime.now().plusDays(3).atZone(ZoneId.systemDefault()));

        return queryFactory.select(formattedDate).distinct()
                .from(theater)
                .where(
                        theater.theaterStartDatetime.goe(startMonthDate),
                        theater.theaterStartDatetime.goe(minAvailDate),
                        theater.theaterEndDatetime.loe(endMonthDate),
                        theater.recruitment.isNull(),
                        theater.theaterCinemaBrandName.in(JPAExpressions.select(brand.brandName)
                                .from(brandMovie)
                                .join(brandMovie.movie, movie)
                                .join(brandMovie.brand, brand)
                                .where(movie.id.eq(movieId))
                        )
                )
                .fetch();
    }

    StringTemplate formattedDate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, {1})",
            theater.theaterStartDatetime,
            ConstantImpl.create("%d")
    );
}
