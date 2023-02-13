package moska.rebora.Main.Repository;

import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepositoryMain {

    List<MoviePageDto> getMovieMainList(String userEmail, String userBirth);
}
