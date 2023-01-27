package moska.rebora.Cinema.Repository;

import moska.rebora.Cinema.Dto.MovieCinemaDto;

import java.util.List;

public interface MovieCinemaRepositoryCustom {

    List<String> getByRegionName(Long movieId);

    List<MovieCinemaDto> getMovieCinemaByCinemaId(Long cinemaId);
}
