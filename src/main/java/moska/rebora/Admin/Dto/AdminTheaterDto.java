package moska.rebora.Admin.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Enum.RecruitmentStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class AdminTheaterDto {

    private Long theaterId;
    private String theaterName;
    private LocalDateTime theaterStartTime;
    private LocalDateTime theaterEndTime;
    private String theaterDay;
    private String theaterCinemaBrandName;
    private String theaterCinemaName;
    private String theaterRegion;
    private Integer theaterPrice;
    private Integer theaterMaxPeople;
    private Integer theaterMinPeople;
    private Integer theaterTime;

    private Long recruitmentId;
    private RecruitmentStatus recruitmentStatus;
    private Integer recruitmentPeople;

    @QueryProjection
    public AdminTheaterDto(Long theaterId,
                           String theaterName,
                           LocalDateTime theaterStartTime,
                           LocalDateTime theaterEndTime,
                           String theaterDay,
                           String theaterRegion,
                           Integer theaterPrice,
                           String theaterCinemaBrandName,
                           String theaterCinemaName,
                           Integer theaterMaxPeople,
                           Integer theaterMinPeople,
                           Integer theaterTime,
                           Long recruitmentId,
                           RecruitmentStatus recruitmentStatus,
                           Integer recruitmentPeople
    ) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterStartTime = theaterStartTime;
        this.theaterEndTime = theaterEndTime;
        this.theaterDay = theaterDay;
        this.theaterCinemaBrandName = theaterCinemaBrandName;
        this.theaterRegion = theaterRegion;
        this.theaterPrice = theaterPrice;
        this.theaterCinemaName = theaterCinemaName;
        this.theaterMaxPeople = theaterMaxPeople;
        this.theaterMinPeople = theaterMinPeople;
        this.theaterTime = theaterTime;
        this.recruitmentId = recruitmentId;
        this.recruitmentStatus = recruitmentStatus;
        this.recruitmentPeople = recruitmentPeople;
    }
}
