package moska.rebora.User.DTO;

import lombok.*;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.UserGrade;
import moska.rebora.User.Entity.User;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDto extends BaseResponse {

    private Long id;
    private String userEmail;
    private String userName;
    private String userNickname;
    private String userPushKey;
    private Boolean userPushYn;
    private Boolean userUseYn;
    private UserGrade userGrade;
    private String userImage;
    private String userBillingKey;
    private String userSnsKind;
    private String userSnsId;
    private Integer notificationCount;

    public UserDto(User user){
        setResult(true);
        id = user.getId();
        userEmail = user.getUserEmail();
        userName = user.getUserName();
        userNickname = user.getUserNickname();
        userPushKey = user.getUserPushKey();
        userPushYn = user.getUserPushYn();
        userUseYn = user.getUserUseYn();
        userGrade = user.getUserGrade();
        userImage = user.getUserImage();
    }
}
