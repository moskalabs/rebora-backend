package moska.rebora.Movie.Repository;

import moska.rebora.Common.Entity.Category;
import moska.rebora.Movie.Dto.MovieDto;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface MovieRepositoryCustom {

    Page<MoviePageDto> getMovieList(@Param("searchCondition") UserSearchCondition searchCondition,
                                    @Param("userEmail") String userEmail,
                                    @Param("userBirth") String userBirth,
                                    @Param("pageable") Pageable pageable
    );

    Page<MoviePageDto> getMovieListByCategory(@Param("searchCondition") UserSearchCondition searchCondition,
                                              @Param("userEmail") String userEmail,
                                              @Param("userBirth") String userBirth,
                                              @Param("pageable") Pageable pageable,
                                              @Param("paramCategory")Category paramCategory
                                              );
}
