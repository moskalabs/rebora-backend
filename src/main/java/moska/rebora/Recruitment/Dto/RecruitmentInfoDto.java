package moska.rebora.Recruitment.Dto;

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
public class RecruitmentInfoDto {
    private String movieName;
    private String movieRecruitmentImage;
    private String movieImage;
    private Integer movieRunningTime;
    private Long movieId;

    private Long theaterId;
    private LocalDateTime theaterStartDatetime;
    private LocalDateTime theaterEndDatetime;
    private String theaterDay;
    private Integer theaterMaxPeople;
    private Integer theaterMinPeople;
    private String theaterName;
    private String theaterCinemaName;
    private String theaterCinemaBrandName;
    private String theaterRegion;
    private Integer theaterPrice;

    private Integer theaterTime;

    private Long userRecruitmentId;
    private Boolean userRecruitmentWish;
    private Boolean userRecruitmentYn;
    private Integer userRecruitmentPeople;

    private Long recruitmentId;
    private LocalDateTime recruitmentEndDate;
    private RecruitmentStatus recruitmentStatus;
    private Boolean recruitmentExposeYn;
    private String recruiterNickname;
    private String recruiterUserImage;
    private Integer recruitmentPeople;
    private String recruitmentIntroduce;
    private Boolean recruitmentCommentUseYn;

    private Page<CommentDto> pageComment;
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
