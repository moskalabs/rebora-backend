package moska.rebora.Cinema.Dto;

import lombok.Data;
import moska.rebora.Movie.Dto.MoviePageDto;

import java.util.List;

@Data
public class CinemaPageDto {

    private Long cinemaId;
    private String cinemaName;
    private String brandName;
    private String regionName;
    private Long theaterCount;
    private List<MovieCinemaDto> movieList;
}
