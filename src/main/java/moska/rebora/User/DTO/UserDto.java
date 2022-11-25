package moska.rebora.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.UserGrade;
import moska.rebora.User.Entity.User;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "유저 Dto")
public class UserDto extends BaseResponse {

    @Schema(description = "유저 아이디")
    private Long id;
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
    @Schema(description = "유저 사용 여부")
    private Boolean userUseYn;
    @Schema(description = "유저 등급 - NORMAL : 보통, ADMIN : 관리자")
    private UserGrade userGrade;
    @Schema(description = "유저 이미지")
    private String userImage;
    @Schema(description = "유저 빌링 키")
    private String userBillingKey;
    @Schema(description = "유저 SNS 종류")
    private String userSnsKind;
    @Schema(description = "유저 SNS 아이디")
    private String userSnsId;
    @Schema(description = "알림 횟수")
    private Integer notificationCount;

    public UserDto(User user) {
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
