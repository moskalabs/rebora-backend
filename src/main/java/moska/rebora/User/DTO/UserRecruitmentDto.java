package moska.rebora.User.DTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;

@Getter
@Setter
public class UserRecruitmentDto extends BaseResponse {

    private Boolean userRecruitmentWish;
    private Boolean userRecruitmentYn;
    private Integer userRecruitmentPeople;
    private Recruitment recruitment;
    private UserDto user;


//    @QueryProjection
//    public UserRecruitmentDto(Boolean userRecruitmentWish, Boolean userRecruitmentYn, Integer userRecruitmentPeople, Recruitment recruitment, User user) {
//        this.user = user;
//        this.userRecruitmentWish = userRecruitmentWish;
//        this.userRecruitmentYn = userRecruitmentYn;
//        this.userRecruitmentPeople = userRecruitmentPeople;
//        this.recruitment = recruitment;
//    }

    @Builder
    public UserRecruitmentDto(Boolean userRecruitmentWish, Boolean userRecruitmentYn, Integer userRecruitmentPeople, Recruitment recruitment, User user) {
        this.userRecruitmentWish = userRecruitmentWish;
        this.userRecruitmentYn = userRecruitmentYn;
        this.userRecruitmentPeople = userRecruitmentPeople;
        this.recruitment = recruitment;
        this.user = new UserDto(user);
    }
}
