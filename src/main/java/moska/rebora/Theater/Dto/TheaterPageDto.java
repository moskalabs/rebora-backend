package moska.rebora.Theater.Dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Enum.RecruitmentStatus;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Schema(description = "상영관 페이지 DTO")
public class TheaterPageDto{
    @Schema(description = "상영관 ID")
    private Long theaterId;
    @Schema(description = "상영관 이름")
    private String theaterName;
    @Schema(description = "상영관 시작 시각")
    private String theaterStartTime;
    @Schema(description = "상영관 종료 시각")
    private String theaterEndTime;
    @Schema(description = "상영관 날짜")
    private String theaterDay;
    @Schema(description = "상영관 극장 브랜드 이름")
    private String theaterCinemaBrandName;
    @Schema(description = "상영관 극장 이름")
    private String theaterCinemaName;
    @Schema(description = "상영관 지역")
    private String theaterRegion;
    @Schema(description = "상영관 가격")
    private Integer theaterPrice;
    @Schema(description = "상영관 최대 인원")
    private Integer theaterMaxPeople;
    @Schema(description = "상영관 최소 인원")
    private Integer theaterMinPeople;
    @Schema(description = "상영관 가능 시간")
    private Integer theaterTime;
    @Schema(description = "모집 ID")
    private Long recruitmentId;
    @Schema(description = "모집 상태")
    private RecruitmentStatus recruitmentStatus;

    @QueryProjection
    public TheaterPageDto(Long theaterId,
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
                          RecruitmentStatus recruitmentStatus
    ) {
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.theaterStartTime = theaterStartTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.theaterEndTime = theaterEndTime.format(DateTimeFormatter.ofPattern("HH:mm"));
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
    }
}
