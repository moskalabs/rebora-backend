package moska.rebora.Theater.Repository;

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TheaterCustom {

    public List<String> getAvailableDate(@Param("theaterRegion") String theaterRegion,
                                         @Param("startMonthDate") LocalDateTime startMonthDate,
                                         @Param("endMonthDate") LocalDateTime endMonthDate,
                                         @Param("movieId") Long movieId

    );
}
