package moska.rebora.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.Entity.User;

@Getter
@Setter
@Schema(description = "유저 로그인 DTO")
@AllArgsConstructor
public class UserLoginDto extends BaseResponse {

    @Schema(description = "토큰")
    private String token;
    @Schema(description = "유저 아이디")
    private Long userId;
    @Schema(description = "유저 이메일")
    private String userEmail;
    @Schema(description = "유저 이름")
    private String userName;
    @Schema(description = "유저 닉네임")
    private String userNickname;
    @Schema(description = "유저 푸쉬 키")
    private String userPushKey;
    @Schema(description = "유저 푸쉬 여부")
    private Boolean userPushYn;
    @Schema(description = "유저 푸쉬 여부")
    private Boolean userPushNightYn;
    @Schema(description = "유저 사용 여부")
    private Boolean userUseYn;
    @Schema(description = "유저 등급 NORMAL - 일반, ADMIN - 관리자")
    private UserGrade userGrade;
    @Schema(description = "유저 이미지")
    private String userImage;
    @Schema(description = "유저 빌링 키")
    private String userBillingKey;
    @Schema(description = "유저 SNS 종류")
    private UserSnsKind userSnsKind;
    @Schema(description = "유저 SNS 아이디")
    private String userSnsId;
    @Schema(description = "알림 개수")
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
        userPushNightYn = user.getUserPushNightYn();
        this.notificationCount = notificationCount;
    }

    public UserLoginDto() {

    }
}
