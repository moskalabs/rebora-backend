package moska.rebora.Theater.Repository;

import moska.rebora.Theater.Entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
}
