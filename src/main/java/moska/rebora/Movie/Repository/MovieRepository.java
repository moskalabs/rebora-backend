package moska.rebora.Movie.Repository;

import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Recruitment.Entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie getMovieById(Long id);
}
