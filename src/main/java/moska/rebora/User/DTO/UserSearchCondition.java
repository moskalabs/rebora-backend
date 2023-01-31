package moska.rebora.User.DTO;

import lombok.Data;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;

import java.time.LocalDateTime;

@Data
public class UserSearchCondition {
    private String searchWord;
    private String searchCondition;
    private boolean createByMe;

    private UserGrade userGrade;
    private UserSnsKind userSnsKind;
    private Boolean userRecruitmentWish;
    private Boolean userMovieWish;
    private RecruitmentStatus recruitmentStatus;
    private Boolean theaterStartDatetime;
    private Long movieId;
    private String orderByMovie;
    private String category;
    private String theaterRegion;
    private String cinemaBrand;
    private LocalDateTime finishTime;
    private Boolean userRecruitmentYn;
}
