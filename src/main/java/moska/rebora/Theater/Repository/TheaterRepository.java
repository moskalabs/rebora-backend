package moska.rebora.Theater.Repository;

import moska.rebora.Admin.Repository.TheaterAdmin;
import moska.rebora.Theater.Entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> , TheaterCustom, TheaterAdmin {
    List<Theater> getTheatersByCinemaId(Long cinemaId);
}
