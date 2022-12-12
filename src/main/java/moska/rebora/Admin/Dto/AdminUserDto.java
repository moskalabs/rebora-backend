package moska.rebora.Admin.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;

import java.time.LocalDateTime;

@Data
public class AdminUserDto {

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
    private Boolean userPushNightYn;
    @Schema(description = "유저 사용 여부")
    private Boolean userUseYn;
    @Schema(description = "유저 등급 - NORMAL : 보통, ADMIN : 관리자")
    private UserGrade userGrade;
    @Schema(description = "유저 이미지")
    private String userImage;
    @Schema(description = "유저 빌링 키")
    private String userBillingKey;
    @Schema(description = "유저 SNS 종류")
    private UserSnsKind userSnsKind;
    @Schema(description = "유저 SNS 아이디")
    private String userSnsId;
    @Schema(description = "알림 횟수")
    private Integer notificationCount;
    private Long participationHistoryCount;
    private Long recruiterCount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
