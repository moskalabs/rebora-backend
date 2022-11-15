package moska.rebora.Movie.Service;

import moska.rebora.Common.BasePageResponse;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface MovieService {

    public BasePageResponse<MoviePageDto> getList(@Param("searchCondition") UserSearchCondition searchCondition,
                                                  @Param("userEmail") String userEmail,
                                                  @Param("pageable") Pageable pageable);
}
