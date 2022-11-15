package moska.rebora.User.Repository;

import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMovieCustom {

    Page<MoviePageDto> getUserMovieList(@Param("searchCondition") UserSearchCondition searchCondition,
                                           @Param("userEmail") String userEmail,
                                           @Param("pageable") Pageable pageable
    );

    List<User> getUserListByMovie(@Param("movieId") Long movieId);
}
