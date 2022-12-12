package moska.rebora.Admin.Repository;

import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface MovieAdmin {

    Page<MoviePageDto> getAdminMovieList(@Param("searchCondition") UserSearchCondition searchCondition,
                                         @Param("pageable") Pageable pageable);

    MoviePageDto getMovieInfo(@Param("movieId") Long movieId);
}
