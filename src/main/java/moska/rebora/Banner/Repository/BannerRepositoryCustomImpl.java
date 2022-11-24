package moska.rebora.Banner.Repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Banner.Dto.BannerCompareDto;
import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.QRecruitment;
import moska.rebora.User.Entity.QUserRecruitment;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Banner.Entity.QBanner.banner;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

public class BannerRepositoryCustomImpl implements BannerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BannerRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BannerDto> getBannerRecently() {

        return queryFactory.select(
                        Projections.fields(
                                BannerDto.class,
                                banner.id.as("banner_id"),
                                banner.bannerExposeYn,
                                banner.bannerMainText,
                                banner.bannerSubText,
                                banner.bannerImage,
                                banner.bannerUrl,
                                recruitment.id.as("recruitment_id"),
                                user.userNickname,
                                user.userImage,
                                movie.movieBannerImage.as("movieBannerImage")
                        ))
                .from(banner)
                .where(banner.bannerExposeYn.eq(true), banner.recruitment.isNotNull())
                .leftJoin(banner.recruitment, recruitment)
                .leftJoin(recruitment.userRecruitmentList, userRecruitment).on(userRecruitment.user.userEmail.eq(recruitment.createdBy))
                .leftJoin(userRecruitment.user, user)
                .leftJoin(recruitment.movie, movie)
                .orderBy(recruitment.createdBy.desc())
                .offset(0)
                .limit(10)
                .fetch();
    }

    @Override
    public List<BannerDto> getBannerUnderCapacity() {

        return queryFactory.select(
                        Projections.fields(
                                BannerDto.class,
                                banner.id.as("banner_id"),
                                banner.bannerExposeYn,
                                banner.bannerMainText,
                                banner.bannerSubText,
                                banner.bannerImage,
                                banner.bannerUrl,
                                recruitment.id.as("recruitment_id"),
                                user.userNickname,
                                user.userImage,
                                movie.movieBannerImage.as("movieBannerImage")
                        ))
                .from(banner)
                .where(banner.bannerExposeYn.eq(true),
                        recruitment.isNotNull(),
                        recruitment.recruitmentStatus.eq(RecruitmentStatus.RECRUITING)
                )
                .leftJoin(banner.recruitment, recruitment)
                .leftJoin(recruitment.theater, theater)
                .leftJoin(recruitment.userRecruitmentList, userRecruitment).on(userRecruitment.user.userEmail.eq(recruitment.createdBy))
                .leftJoin(userRecruitment.user, user)
                .leftJoin(recruitment.movie, movie)
                .offset(0)
                .limit(10)
                .fetch();
    }

    @Override
    public List<BannerCompareDto> getCompareBannerList() {
        return queryFactory.select(
                        Projections.fields(
                                BannerCompareDto.class,
                                banner.id.as("bannerId"),
                                theater.theaterMinPeople.subtract(recruitment.recruitmentPeople).as("resultCount")
                        )
                )
                .from(banner)
                .join(banner.recruitment, recruitment)
                .join(recruitment.theater, theater)
                .where(banner.id.ne(1L))
                .orderBy(theater.theaterMinPeople.subtract(recruitment.recruitmentPeople).asc())
                .offset(0)
                .limit(10)
                .fetch();
    }
}
