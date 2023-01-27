package moska.rebora.Cinema.Dto;

import lombok.Data;
import moska.rebora.Movie.Entity.Movie;

@Data
public class MovieCinemaDto {

    private String movieName;
    private Long movieId;
    private boolean movieCinemaYn;

    public MovieCinemaDto() {

    }

    public MovieCinemaDto(Movie movie) {
        this.movieName = movie.getMovieName();
        this.movieId = movie.getId();
        this.movieCinemaYn = false;
    }
}
