package moska.rebora.User.Service;

import org.springframework.data.repository.query.Param;

public interface WishService {

    void wishRecruitment(
            @Param("userRecruitmentId") Long userRecruitmentId,
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("userRecruitmentWish") Boolean userRecruitmentWish
    );

    void wishMovie(@Param("userMovieId") Long userMovieId,
                   @Param("movieId") Long movieId,
                   @Param("userEmail") String userEmail,
                   @Param("userMovieWish") Boolean userRecruitmentWish);
}
