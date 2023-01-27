package moska.rebora.Cinema.Repository;

import moska.rebora.Admin.Repository.AdminCinemaRepository;
import moska.rebora.Cinema.Entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long>, AdminCinemaRepository {

    Optional<Cinema> getCinemaByCinemaName(String cinemaName);

    Optional<Cinema> getCinemaByCinemaNameAndRegionName(String cinemaName, String regionName);
}
