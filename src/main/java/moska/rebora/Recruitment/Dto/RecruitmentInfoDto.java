package moska.rebora.Recruitment.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Comment.Dto.CommentDto;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.User.DTO.UserImageListDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "모집 정보 Dto")
public class RecruitmentInfoDto {
    @Schema(description = "영화 이름")
    private String movieName;
    @Schema(description = "영화 모집 이미지")
    private String movieRecruitmentImage;
    @Schema(description = "영화 이미지")
    private String movieImage;
    @Schema(description = "영화 러닝 타임")
    private Integer movieRunningTime;
    @Schema(description = "영화 ID")
    private Long movieId;
    @Schema(description = "영화 ID")
    private Long theaterId;
    @Schema(description = "상영관 시작 시각")
    private LocalDateTime theaterStartDatetime;
    @Schema(description = "상영관 종료 시각")
    private LocalDateTime theaterEndDatetime;
    @Schema(description = "상영관 요일")
    private String theaterDay;
    @Schema(description = "상영관 최대 인원")
    private Integer theaterMaxPeople;
    @Schema(description = "상영관 최소 인원")
    private Integer theaterMinPeople;
    @Schema(description = "상영관 이름")
    private String theaterName;
    @Schema(description = "상영관 극장 이름")
    private String theaterCinemaName;
    @Schema(description = "상영관 극장 브랜드 이름")
    private String theaterCinemaBrandName;
    @Schema(description = "상영관 지역")
    private String theaterRegion;
    @Schema(description = "상영관 가격")
    private Integer theaterPrice;
    @Schema(description = "상영 가능 시간")
    private Integer theaterTime;
    @Schema(description = "유저 모집 ID")
    private Long userRecruitmentId;
    @Schema(description = "모집 찜 여부")
    private Boolean userRecruitmentWish;
    @Schema(description = "유저 신쳥 여부")
    private Boolean userRecruitmentYn;
    @Schema(description = "유저 신청 인원")
    private Integer userRecruitmentPeople;
    @Schema(description = "모집 아이디")
    private Long recruitmentId;
    @Schema(description = "모집 마감 날짜")
    private LocalDateTime recruitmentEndDate;
    @Schema(description = "모집 상태")
    private RecruitmentStatus recruitmentStatus;
    @Schema(description = "모집 노출 여부")
    private Boolean recruitmentExposeYn;
    @Schema(description = "모집 닉네임")
    private String recruiterNickname;
    @Schema(description = "모집장 유지 이미지")
    private String recruiterUserImage;
    @Schema(description = "모집 인원")
    private Integer recruitmentPeople;
    @Schema(description = "모집 소개")
    private String recruitmentIntroduce;
    @Schema(description = "모집 댓글 여부")
    private Boolean recruitmentCommentUseYn;
    @Schema(description = "댓글 리스트")
    private Page<CommentDto> pageComment;
    @Schema(description = "모집 신청 유저 이미지")
    private List<UserImageListDto> userImageList;

    public void addPageComment(Page<CommentDto> pageComment) {
        this.pageComment = pageComment;
    }

    public void addUserImageList(List<UserImageListDto> userImageList) {
        this.userImageList = userImageList;
    }

    public RecruitmentInfoDto(String movieName, String movieRecruitmentImage, Long movieId, Long theaterId, LocalDateTime theaterStartDatetime, LocalDateTime theaterEndDatetime, String theaterDay, Integer theaterMaxPeople, Integer theaterMinPeople, String theaterCinemaName, String theaterCinemaBrandName, String theaterRegion, Integer theaterPrice, Integer theaterTime, Long userRecruitmentId, Boolean userRecruitmentWish, Boolean userRecruitmentYn, Integer userRecruitmentPeople, Long recruitmentId, LocalDateTime recruitmentEndDate, RecruitmentStatus recruitmentStatus, String recruiterNickname, String recruiterUserImage, Integer recruitmentPeople, Boolean recruitmentCommentUseYn) {
        this.movieName = movieName;
        this.movieRecruitmentImage = movieRecruitmentImage;
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
        this.theaterPrice = theaterPrice;
        this.theaterTime = theaterTime;
        this.userRecruitmentId = userRecruitmentId;
        this.userRecruitmentWish = userRecruitmentWish;
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
        this.recruitmentId = recruitmentId;
        this.recruitmentEndDate = recruitmentEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.recruiterNickname = recruiterNickname;
        this.recruiterUserImage = recruiterUserImage;
        this.recruitmentPeople = recruitmentPeople;
        this.recruitmentCommentUseYn = recruitmentCommentUseYn;
    }
}
