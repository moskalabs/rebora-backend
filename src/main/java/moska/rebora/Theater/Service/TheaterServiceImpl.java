package moska.rebora.Theater.Service;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Theater.Dto.TheaterMovieDto;
import moska.rebora.Theater.Repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class TheaterServiceImpl implements TheaterService {

    @Autowired
    TheaterRepository theaterRepository;

    public Page<TheaterMovieDto> getTheaterListByMovie(Long movieId) {

        return null;
    }

    @Override
    public List<String> getAvailableDate(String theaterRegion, String selectMonth, Long movieId) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        log.info("selectMonth={}", selectMonth);

        LocalDate parseSelectMonth = LocalDate.parse(selectMonth);
        log.info("parseSelectMonth={}", parseSelectMonth);

        LocalDateTime startMonthDate = parseSelectMonth.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endMonthDate = parseSelectMonth.withDayOfMonth(parseSelectMonth.lengthOfMonth()).atTime(LocalTime.MAX);
        log.info("startMonthDate={}", startMonthDate);
        log.info("endMonthDate={}", endMonthDate);

        return theaterRepository.getAvailableDate(theaterRegion, startMonthDate, endMonthDate, movieId);
    }
}
