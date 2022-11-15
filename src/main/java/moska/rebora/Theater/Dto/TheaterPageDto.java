package moska.rebora.Theater.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Enum.RecruitmentStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class TheaterPageDto{

    private Long theaterId;
    private String theaterName;
    private String theaterStartTime;
    private String theaterEndTime;
    private String theaterDay;
    private String theaterCinemaBrandName;
    private String theaterCinemaName;
    private Integer theaterMaxPeople;
    private Integer theaterMinPeople;
    private Integer theaterTime;

    private Long recruitmentId;
    private RecruitmentStatus recruitmentStatus;

    @QueryProjection
    public TheaterPageDto(Long theaterId, String theaterName, LocalDateTime theaterStartTime, LocalDateTime theaterEndTime, String theaterDay, String theaterCinemaBrandName, String theaterCinemaName, Integer theaterMaxPeople, Integer theaterMinPeople, Integer theaterTime, Long recruitmentId, RecruitmentStatus recruitmentStatus) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterStartTime = theaterStartTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.theaterEndTime = theaterEndTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.theaterDay = theaterDay;
        this.theaterCinemaBrandName = theaterCinemaBrandName;
        this.theaterCinemaName = theaterCinemaName;
        this.theaterMaxPeople = theaterMaxPeople;
        this.theaterMinPeople = theaterMinPeople;
        this.theaterTime = theaterTime;
        this.recruitmentId = recruitmentId;
        this.recruitmentStatus = recruitmentStatus;
    }
}
