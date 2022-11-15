package moska.rebora.User.DTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.User.Entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRecruitmentListDto {

    private Long userRecruitmentId;
    private Boolean userRecruitmentWish;
    private Boolean userRecruitmentYn;
    private Integer userRecruitmentPeople;

    private String movieName;
    private String movieImage;
    private Long movieId;

    private Long theaterId;
    private LocalDateTime theaterStartDatetime;
    private LocalDateTime theaterEndDatetime;
    private String theaterDay;
    private Integer theaterMaxPeople;
    private Integer theaterMinPeople;
    private String theaterCinemaName;
    private String theaterCinemaBrandName;
    private String theaterRegion;

    private Long recruitmentId;
    private LocalDateTime recruitmentEndDate;
    private RecruitmentStatus recruitmentStatus;
    private String recruiterNickname;
    private String recruiterUserImage;
    private List<UserImageListDto> userImageList;
    private Integer recruitmentPeople;

    public void addUserImage(List<UserImageListDto> userImageList) {
        this.userImageList = userImageList;
    }

    public UserRecruitmentListDto(Long userRecruitmentId, Boolean userRecruitmentWish, Boolean userRecruitmentYn, Integer userRecruitmentPeople, String movieName, String movieImage, Long movieId, Long theaterId, LocalDateTime theaterStartDatetime, LocalDateTime theaterEndDatetime, String theaterDay, Integer theaterMaxPeople, Integer theaterMinPeople, String theaterCinemaName, String theaterCinemaBrandName, String theaterRegion, Long recruitmentId, LocalDateTime recruitmentEndDate, RecruitmentStatus recruitmentStatus, String recruiterNickname, String recruiterUserImage, Integer recruitmentPeople) {
        this.userRecruitmentId = userRecruitmentId;
        this.userRecruitmentWish = userRecruitmentWish;
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
        this.movieName = movieName;
        this.movieImage = movieImage;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.theaterStartDatetime = theaterStartDatetime;
        this.theaterEndDatetime = theaterEndDatetime;
        this.theaterDay = theaterDay;
        this.theaterMaxPeople = theaterMaxPeople;
        this.theaterMinPeople = theaterMinPeople;
        this.theaterCinemaName = theaterCinemaName;
        this.theaterCinemaBrandName = theaterCinemaBrandName;
        this.theaterRegion = theaterRegion;
        this.recruitmentId = recruitmentId;
        this.recruitmentEndDate = recruitmentEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.recruiterNickname = recruiterNickname;
        this.recruiterUserImage = recruiterUserImage;
        this.recruitmentPeople = recruitmentPeople;
    }
}
