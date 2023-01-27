package moska.rebora.Admin.Service;

import moska.rebora.Cinema.Dto.CinemaPageDto;
import moska.rebora.Common.BaseResponse;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminCinemaService {

    Page<CinemaPageDto> getCinemaPage(Pageable pageable, UserSearchCondition userSearchCondition);

    CinemaPageDto getCinemaInfo(Long cinemaId);

    void saveInfo(
            @Param("cinemaId") Long cinemaId,
            @Param("brandName") String brandName,
            @Param("regionName") String regionName,
            @Param("cinemaName") String cinemaName,
            @Param("movieIdList") List<Long> movieIdList
    );
}
