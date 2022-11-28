package moska.rebora.User.DTO;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "유저_모집 리스트 DTO")
public class

UserRecruitmentListDto {

    @Schema(description = "유저_모집 아이디")
    private Long userRecruitmentId;
    @Schema(description = "유저_모집 찜 여부")
    private Boolean userRecruitmentWish;
    @Schema(description = "유저_모집 참여 여부")
    private Boolean userRecruitmentYn;
    @Schema(description = "유저_모집 참여 인원")
    private Integer userRecruitmentPeople;

    @Schema(description = "영화 이름")
    private String movieName;
    @Schema(description = "영화 이미지 경로")
    private String movieImage;

    @Schema(description = "영화 아이디")
    private Long movieId;

    @Schema(description = "상영관 정보")
    private Long theaterId;
    @Schema(description = "상영관 시작 시간")
    private LocalDateTime theaterStartDatetime;
    @Schema(description = "상영관 종료 시간")
    private LocalDateTime theaterEndDatetime;
    @Schema(description = "상영관 요일")
    private String theaterDay;
    @Schema(description = "상영관 최대 인원")
    private Integer theaterMaxPeople;
    @Schema(description = "상영관 최소 인원")
    private Integer theaterMinPeople;
    @Schema(description = "상영관 극장 이름")
    private String theaterCinemaName;
    @Schema(description = "상영관 브랜드 이름")
    private String theaterCinemaBrandName;
    @Schema(description = "상영관 지역")
    private String theaterRegion;

    @Schema(description = "모집 아이디")
    private Long recruitmentId;
    @Schema(description = "모집 종료 날짜")
    private LocalDateTime recruitmentEndDate;
    @Schema(description = "모집 상태 - CANCEL : 취소, WAIT : 대기, RECRUITING : 모집중, CONFIRMATION : 모집확인, COMPLETED : 상영완료")
    private RecruitmentStatus recruitmentStatus;
    @Schema(description = "모집장 닉네임")
    private String recruiterNickname;
    @Schema(description = "모집장 유저 이미지")
    private String recruiterUserImage;
    @Schema(description = "모집 참여 이미지 목록")
    private List<UserImageListDto> userImageList;
    @Schema(description = "모집 참여 인원")
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
