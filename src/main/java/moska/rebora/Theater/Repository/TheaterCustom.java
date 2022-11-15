package moska.rebora.Theater.Repository;

import moska.rebora.Theater.Dto.TheaterPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TheaterCustom {

    List<String> getAvailableDate(@Param("theaterRegion") String theaterRegion,
                                         @Param("startMonthDate") LocalDateTime startMonthDate,
                                         @Param("endMonthDate") LocalDateTime endMonthDate,
                                         @Param("movieId") Long movieId

    );

    Page<TheaterPageDto> getPageTheater(@Param("theaterRegion") String theaterRegion,
                                        @Param("selectDate") LocalDate selectDate,
                                        @Param("movieId") Long movieId,
                                        @Param("pageable")Pageable pageable
    );
}
