package moska.rebora.Theater.Service;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Theater.Dto.TheaterMovieDto;
import moska.rebora.Theater.Dto.TheaterPageDto;
import moska.rebora.Theater.Repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MovieRepository movieRepository;

    @Autowired
    TheaterRepository theaterRepository;

    public Page<TheaterMovieDto> getTheaterListByMovie(Long movieId) {

        return null;
    }

    @Override
    public List<String> getAvailableDate(String theaterRegion, String selectMonth, Long movieId) {

        log.info("selectMonth={}", selectMonth);

        String[] selectMonthList = selectMonth.split("-");
        String monthSelect = selectMonthList[1];
        String daySelect = selectMonthList[2];
        if (Integer.parseInt(monthSelect) < 10) {
            if (!monthSelect.equals("01") &&
                    !monthSelect.equals("02") &&
                    !monthSelect.equals("03") &&
                    !monthSelect.equals("04") &&
                    !monthSelect.equals("05") &&
                    !monthSelect.equals("06") &&
                    !monthSelect.equals("07") &&
                    !monthSelect.equals("08") &&
                    !monthSelect.equals("09")
            ) {
                monthSelect = String.format("%02d", Integer.parseInt(monthSelect));
            }
        }

        if (Integer.parseInt(daySelect) < 10) {
            if (!daySelect.equals("01") &&
                    !daySelect.equals("02") &&
                    !daySelect.equals("03") &&
                    !daySelect.equals("04") &&
                    !daySelect.equals("05") &&
                    !daySelect.equals("06") &&
                    !daySelect.equals("07") &&
                    !daySelect.equals("08") &&
                    !daySelect.equals("09")
            ) {
                daySelect = String.format("%02d", Integer.parseInt(daySelect));
            }
        }

        log.info("monthSelect={}", monthSelect);
        log.info("daySelect={}", daySelect);
        String formatDate = selectMonthList[0] + "-" + monthSelect + "-" + daySelect;

        LocalDate parseSelectMonth = LocalDate.parse(formatDate);
        log.info("parseSelectMonth={}", parseSelectMonth);

        LocalDateTime startMonthDate = parseSelectMonth.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endMonthDate = parseSelectMonth.withDayOfMonth(parseSelectMonth.lengthOfMonth()).atTime(LocalTime.MAX);
        log.info("startMonthDate={}", startMonthDate);
        log.info("endMonthDate={}", endMonthDate);

        return theaterRepository.getAvailableDate(theaterRegion, startMonthDate, endMonthDate, movieId);
    }

    @Override
    @Transactional
    public Page<TheaterPageDto> getPageTheater(String theaterRegion,
                                               String selectDate,
                                               Long movieId,
                                               Pageable pageable) {

        return theaterRepository.getPageTheater(theaterRegion, LocalDate.parse(selectDate), movieId, pageable);
    }
}
