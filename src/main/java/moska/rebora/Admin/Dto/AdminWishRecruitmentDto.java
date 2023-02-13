package moska.rebora.Admin.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminWishRecruitmentDto {

    Long movieId;
    String movieName;
    Long theaterId;
    String theaterRegion;
    String theaterCinemaName;
    String theaterCinemaBrandName;
    String theaterName;
    String theaterDay;
    LocalDateTime theaterStartDatetime;
    LocalDateTime theaterEndDatetime;

    public AdminWishRecruitmentDto() {
    }
}
