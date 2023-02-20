package moska.rebora.Admin.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Admin.Dto.AdminBrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static moska.rebora.Cinema.Entity.QBrand.brand;
import static moska.rebora.Cinema.Entity.QCinema.cinema;

public class AdminBrandRepositoryImpl implements AdminBrandRepository {

    private final JPAQueryFactory queryFactory;

    public AdminBrandRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminBrandDto> getPageBrandList(Pageable pageable) {

        List<AdminBrandDto> content = queryFactory.select(Projections.fields(
                        AdminBrandDto.class,
                        brand.id.as("brandId"),
                        brand.brandName,
                        brand.regDate,
                        brand.modDate
                ))
                .from(brand)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(brand.id.count())
                .from(brand);

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }
}
