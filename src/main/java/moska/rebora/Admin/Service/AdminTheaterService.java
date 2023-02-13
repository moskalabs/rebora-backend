package moska.rebora.Admin.Service;

import org.springframework.data.repository.query.Param;

public interface AdminTheaterService {

    void createTheater(
            @Param("regionName") String regionName,
            @Param("brandName") String brandName,
            @Param("cinemaId") Long cinemaId,
            @Param("theaterName") String theaterName,
            @Param("theaterDate") String theaterDate,
            @Param("theaterStartHour") String theaterStartHour,
            @Param("theaterStartMinute") String theaterStartMinute,
            @Param("theaterEndHour") String theaterEndHour,
            @Param("theaterEndMinute") String theaterEndMinute,
            @Param("theaterMinPeople") Integer theaterMinPeople,
            @Param("theaterMaxPeople") Integer theaterMaxPeople,
            @Param("theaterTime") Integer theaterTime,
            @Param("theaterPrice") Integer theaterPrice
    );
}
