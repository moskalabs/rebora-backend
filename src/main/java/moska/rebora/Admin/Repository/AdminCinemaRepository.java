package moska.rebora.Admin.Repository;

import moska.rebora.Cinema.Dto.CinemaPageDto;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminCinemaRepository {

    Page<CinemaPageDto> getCinemaPage(Pageable pageable, UserSearchCondition userSearchCondition);

    CinemaPageDto getCinemaInfo(Long cinemaId);

    List<Cinema> getCinemaListByMovieId(Long movieId);
}
