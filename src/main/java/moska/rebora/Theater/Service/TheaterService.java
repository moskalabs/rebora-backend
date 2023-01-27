package moska.rebora.Theater.Service;

import moska.rebora.Theater.Dto.TheaterPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TheaterService {

    List<String> getAvailableDateRegion(
            @Param("movieId") Long movieId
    );

    List<String> getAvailableDate(
            @Param("theaterRegion") String theaterRegion,
            @Param("selectMonth") String selectMonth,
            @Param("movieId") Long movieId
    );

    Page<TheaterPageDto> getPageTheater(@Param("theaterRegion") String theaterRegion,
                                        @Param("selectDate") String selectDate,
                                        @Param("movieId") Long movieId,
                                        @Param("pageable") Pageable pageable);
}
