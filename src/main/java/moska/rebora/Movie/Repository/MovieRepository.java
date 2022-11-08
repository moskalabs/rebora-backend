package moska.rebora.Movie.Repository;

import moska.rebora.Main.Repository.MovieRepositoryMain;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Recruitment.Entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryMain, MovieRepositoryCustom {

    Movie getMovieById(Long id);
}
