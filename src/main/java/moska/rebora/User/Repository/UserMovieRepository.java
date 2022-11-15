package moska.rebora.User.Repository;

import moska.rebora.Movie.Entity.Movie;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMovieRepository extends JpaRepository<UserMovie, Long>, UserMovieCustom {

    Optional<UserMovie> getUserMovieByUserAndMovie(User user, Movie movie);
}
