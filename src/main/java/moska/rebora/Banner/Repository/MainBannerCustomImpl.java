package moska.rebora.Banner.Repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.User.Entity.QUser;

import javax.persistence.EntityManager;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static moska.rebora.Banner.Entity.QBanner.banner;
import static moska.rebora.Banner.Entity.QMainBanner.mainBanner;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

public class MainBannerCustomImpl implements MainBannerCustom {

    private final JPAQueryFactory queryFactory;

    public MainBannerCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BannerDto> getMainBanner() {

        QUser recruiterUserImage = new QUser("recruiterUserImage");
        QUser recruiterUserNickname = new QUser("recruiterUserNickname");

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
                                ExpressionUtils.as(select(recruiterUserImage.userImage.as("userImage"))
                                        .from(recruiterUserImage)
                                        .where(recruiterUserImage.userEmail.eq(recruitment.createdBy)), "userImage"),
                                ExpressionUtils.as(select(recruiterUserNickname.userNickname.as("userNickname"))
                                        .from(recruiterUserNickname)
                                        .where(recruiterUserNickname.userEmail.eq(recruitment.createdBy)), "userNickname"),
                                movie.movieBannerImage.as("movieBannerImage")
                        ))
                .from(mainBanner)
                .leftJoin(mainBanner.banner, banner)
                .where(banner.bannerExposeYn.eq(true), banner.recruitment.isNotNull())
                .leftJoin(banner.recruitment, recruitment)
                .leftJoin(recruitment.movie, movie)
                .offset(0)
                .limit(10)
                .fetch();
    }


}
