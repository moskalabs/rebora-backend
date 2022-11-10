package moska.rebora.Theater.Service;

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TheaterService {

    List<String> getAvailableDate(String theaterRegion, String selectMonth, Long movieId);
}
