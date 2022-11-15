package moska.rebora.User.Service;

import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface WishService {

    void wishRecruitment(
            @Param("userRecruitmentId") Long userRecruitmentId,
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("userRecruitmentWish") Boolean userRecruitmentWish
    );

    void wishMovie(
            @Param("userMovieId") Long userMovieId,
            @Param("movieId") Long movieId,
            @Param("userEmail") String userEmail,
            @Param("userMovieWish") Boolean userRecruitmentWish
    );

    Page<UserRecruitmentListDto> getRecruitmentList(
            @Param("pageable") Pageable pageable,
            @Param("userEmail") String userEmail
    );

    Page<MoviePageDto> getMovieList(@Param("pageable") Pageable pageable,
                                    @Param("userEmail") String userEmail);
}
