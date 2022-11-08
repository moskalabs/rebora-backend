package moska.rebora.User.DTO;

import lombok.Data;
import moska.rebora.Enum.RecruitmentStatus;

@Data
public class UserSearchCondition {

    private boolean createByMe;
    private RecruitmentStatus recruitmentStatus;
    private Boolean theaterStartDatetime;
    private String searchWord;
    private String orderByMovie;
    private String category;
}
