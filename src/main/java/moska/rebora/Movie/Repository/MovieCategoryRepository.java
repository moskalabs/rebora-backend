package moska.rebora.Movie.Repository;

import moska.rebora.Common.Entity.Category;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Entity.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieCategoryRepository extends JpaRepository<MovieCategory, Long> {


    List<MovieCategory> getMovieCategoryByMovie(Movie movie);
}
