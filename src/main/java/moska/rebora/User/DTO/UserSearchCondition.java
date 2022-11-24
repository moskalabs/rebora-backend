package moska.rebora.User.DTO;

import lombok.Data;
import moska.rebora.Enum.RecruitmentStatus;

import java.time.LocalDateTime;

@Data
public class UserSearchCondition {

    private boolean createByMe;
    private RecruitmentStatus recruitmentStatus;
    private Boolean theaterStartDatetime;
    private String searchWord;
    private String orderByMovie;
    private String category;
    private String theaterRegion;
    private Long movieId;
    private Boolean userRecruitmentWish;
    private Boolean userMovieWish;
    private LocalDateTime finishTime;
}
