package moska.rebora.User.Repository;

import moska.rebora.User.Entity.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMovieRepository extends JpaRepository<UserMovie, Long> {
}
