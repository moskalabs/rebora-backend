package moska.rebora.User.Entity;

import lombok.*;
import moska.rebora.Common.BaseTimeEntity;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "i_user_email_use", columnList = "userEmail,userUseYn"),
        @Index(name = "search_recruitment", columnList = "user_id,userEmail")
}
)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("유저 아이디")
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(unique = true)
    @Comment("유저 이메일")
    private String userEmail;

    @Column
    @Comment("패스워드")
    private String password;

    @Column(length = 50, nullable = false)
    @Comment("유저 이름")
    private String userName;

    @Column(length = 50, nullable = false, unique = true)
    @Comment("유저 닉네임")
    private String userNickname;

    @Column(columnDefinition = "LONGTEXT")
    @Comment("유저 푸쉬 키")
    private String userPushKey;

    @Column(nullable = true, columnDefinition = "boolean default false")
    @Comment("유저 푸쉬 여부")
    private Boolean userPushYn;

    @Column(nullable = true, columnDefinition = "boolean default false")
    @Comment("유저 푸쉬 야간")
    private Boolean userPushNightYn;

    @Column(nullable = false)
    @Comment("유저 사용 여부")
    private Boolean userUseYn;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Comment("유저 등급")
    private UserGrade userGrade;

    @Column
    @Comment("유저 이미지")
    private String userImage;

    @Column
    @Comment("유저 빌링 키")
    private String userBillingKey;

    @Column
    @Comment("유저 고객 아이디")
    private String userCustomerId;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 10)
    @Comment("유저 SNS 종류")
    private UserSnsKind userSnsKind;

    @Column
    @Comment("유저 SNS ID")
    private String userSnsId;

    @ColumnDefault("0")
    @Column
    @Comment("유저 본인 인증 여부")
    private Boolean isAuthenticated;

    @ColumnDefault("0")
    @Column
    @Comment("유저 나이")
    private Integer userAge;
    @ColumnDefault("'1990-01-01'")
    @Column
    @Comment("유저 생년월일")
    private String userBirth;

    @Column(length = 50)
    @Comment("유저 핸드폰 번호")
    private String userPhone;

    public User() {
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeUseYn(Boolean userUseYn) {
        this.userUseYn = userUseYn;
    }

    public void changePushYn(Boolean userPushYn, String userPushKey) {
        if (userPushYn) {
            this.userPushYn = true;
        } else {
            this.userPushYn = false;
            this.userPushNightYn = false;
        }

        if (!userPushKey.equals("")) {
            this.userPushKey = userPushKey;
        }
    }

    public void changePushNightYn(Boolean userPushNightYn, String userPushKey) {
        this.userPushNightYn = userPushNightYn;

        if (!userPushKey.equals("")) {
            this.userPushKey = userPushKey;
        }
    }

    public void changeUserInfo(String userImage, String password, String userNickname) {
        if (!userImage.equals("")) {
            this.userImage = userImage;
        }
        if (!password.equals("")) {
            this.password = password;
        }
        if (!userNickname.equals("")) {
            this.userNickname = userNickname;
        }
    }

    public void changeUserBillingKey(String userBillingKey, String userCustomerId) {
        this.userBillingKey = userBillingKey;
        this.userCustomerId = userCustomerId;
    }

    public void changeAdminUserInfo(
            String userEmail,
            String userName,
            String userImage,
            Boolean userPushYn,
            Boolean userPushNightYn,
            Boolean userUseYn,
            String userGrade
    ) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userImage = userImage;
        this.userPushYn = userPushYn;
        this.userPushNightYn = userPushNightYn;
        this.userUseYn = userUseYn;
        this.userGrade = UserGrade.valueOf(userGrade);
    }

    public void addUserSns(
            UserSnsKind userSnsKind,
            String userSnsId
    ) {
        this.userSnsKind = userSnsKind;
        this.userSnsId = userSnsId;
    }

    public void withdrawalUser() {
        this.userUseYn = false;
    }

    public void updateUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void changePushKey(String userPushKey){
        if (!userPushKey.equals("")){
            this.userPushKey = userPushKey;
        }
    }

    @Builder
    public User(Long id, String userEmail, String password, String userName, String userNickname, String userPushKey, Boolean userPushYn, Boolean userPushNightYn, Boolean userUseYn, UserGrade userGrade, String userImage, String userBillingKey, String userCustomerId, UserSnsKind userSnsKind, String userSnsId, Boolean isAuthenticated, Integer userAge, String userBirth, String userPhone) {
        this.id = id;
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPushKey = userPushKey;
        this.userPushYn = userPushYn;
        this.userPushNightYn = userPushNightYn;
        this.userUseYn = userUseYn;
        this.userGrade = userGrade;
        this.userImage = userImage;
        this.userBillingKey = userBillingKey;
        this.userCustomerId = userCustomerId;
        this.userSnsKind = userSnsKind;
        this.userSnsId = userSnsId;
        this.isAuthenticated = isAuthenticated;
        this.userAge = userAge;
        this.userBirth = userBirth;
        this.userPhone = userPhone;
    }
}
