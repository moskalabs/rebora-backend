package moska.rebora.User.DTO;

import lombok.Data;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;

import java.time.LocalDateTime;

@Data
public class UserSearchCondition {

    private boolean createByMe;
    private RecruitmentStatus recruitmentStatus;
    private Boolean theaterStartDatetime;
    private String searchWord;
    private String searchCondition;
    private UserGrade userGrade;
    private UserSnsKind userSnsKind;
    private String orderByMovie;
    private String category;
    private String theaterRegion;
    private String cinemaBrand;
    private Long movieId;
    private Boolean userRecruitmentWish;
    private Boolean userMovieWish;
    private LocalDateTime finishTime;

}
