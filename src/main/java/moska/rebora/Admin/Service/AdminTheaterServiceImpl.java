package moska.rebora.Admin.Service;


import com.mchange.util.DuplicateElementException;
import lombok.AllArgsConstructor;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Repository.CinemaRepository;
import moska.rebora.Common.Util;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminTheaterServiceImpl implements AdminTheaterService {

    Util util;

    CinemaRepository cinemaRepository;

    TheaterRepository theaterRepository;

    @Override
    public void createTheater(
            String regionName,
            String brandName,
            Long cinemaId,
            String theaterName,
            String theaterDate,
            String theaterStartHour,
            String theaterStartMinute,
            String theaterEndHour,
            String theaterEndMinute,
            Integer theaterMinPeople,
            Integer theaterMaxPeople,
            Integer theaterTime,
            Integer theaterPrice
    ) {
        Optional<Cinema> optionalCinema = cinemaRepository.findById(cinemaId);

        if(optionalCinema.isEmpty()){
            throw new NullPointerException("존재하지 않는 극장입니다.");
        }

        Cinema cinema = optionalCinema.get();

        String[] dateList = theaterDate.split("-");
        Integer year = Integer.valueOf(dateList[0]);
        Integer month = Integer.valueOf(dateList[1]);
        Integer date = Integer.valueOf(dateList[2]);

        LocalDateTime theaterStartDatetime = util.convertDateTime(year, month, date, Integer.valueOf(theaterStartHour), Integer.valueOf(theaterStartMinute));
        LocalDateTime theaterEndDatetime = util.convertDateTime(year, month, date, Integer.valueOf(theaterEndHour), Integer.valueOf(theaterEndMinute));

        Long theaterCount = theaterRepository.checkTheaterCsv(regionName, brandName, cinema.getCinemaName(), theaterName, theaterStartDatetime, theaterEndDatetime);

        if(theaterCount != 0L){
            throw new DuplicateElementException("이미 겹치거나 중복된 시간입니다.");
        }

        Theater theater = Theater.builder()
                .theaterRegion(regionName)
                .theaterDay(theaterStartDatetime.getDayOfWeek().getDisplayName(TextStyle.NARROW, Locale.KOREA))
                .theaterCinemaName(cinema.getCinemaName())
                .theaterTime(theaterTime)
                .theaterPrice(theaterPrice)
                .theaterName(theaterName)
                .theaterStartDatetime(theaterStartDatetime)
                .theaterEndDatetime(theaterEndDatetime)
                .theaterMinPeople(theaterMinPeople)
                .theaterMaxPeople(theaterMaxPeople)
                .theaterCinemaBrandName(brandName)
                .cinema(cinema)
                .build();

        theaterRepository.save(theater);
    }
}
