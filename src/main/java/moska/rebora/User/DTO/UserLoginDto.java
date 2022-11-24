package moska.rebora.User.DTO;

import lombok.Builder;
import lombok.Getter;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.UserGrade;
import moska.rebora.User.Entity.User;

@Getter
public class UserLoginDto extends BaseResponse {

    private String token;
    private Long userId;
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

    @Builder
    public UserLoginDto(String token, Boolean result, String errorCode, String message, User user, Integer notificationCount) {
        setResult(result);
        setErrorCode(errorCode);
        setMessage(message);
        this.token = token;
        this.userId = user.getId();
        userEmail = user.getUserEmail();
        userName = user.getUserName();
        userNickname = user.getUserNickname();
        userPushKey = user.getUserPushKey();
        userPushYn = user.getUserPushYn();
        userUseYn = user.getUserUseYn();
        userGrade = user.getUserGrade();
        userImage = user.getUserImage();
        this.notificationCount = notificationCount;
    }
}
