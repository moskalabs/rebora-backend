package moska.rebora.Cinema.Repository;

import moska.rebora.Cinema.Entity.MovieCinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieCinemaRepository extends JpaRepository<MovieCinema, Long>, MovieCinemaRepositoryCustom {

    List<MovieCinema> getMovieCinemasByCinemaId(Long cinemaId);

    List<MovieCinema> getMovieCinemasByMovieId(Long movieId);
}
