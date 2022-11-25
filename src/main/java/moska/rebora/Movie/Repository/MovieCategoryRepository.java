package moska.rebora.Movie.Repository;

import moska.rebora.Movie.Entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
}
