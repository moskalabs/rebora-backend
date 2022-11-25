package moska.rebora.User.Entity;

import lombok.*;
import moska.rebora.Common.BaseTimeEntity;
import moska.rebora.Enum.UserGrade;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(length = 100)
    @Comment("유저 푸쉬 키")
    private String userPushKey;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Comment("유저 푸쉬 여부")
    private Boolean userPushYn;

    @Column(nullable = false, columnDefinition = "boolean default false")
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

    @Column(length = 10)
    @Comment("유저 SNS 종류")
    private String userSnsKind;

    @Column
    @Comment("유저 SNS ID")
    private String userSnsId;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changePushYn(Boolean userPushYn) {
        if (userPushYn) {
            this.userPushYn = true;
        } else {
            this.userPushYn = false;
            this.userPushNightYn = false;
        }
    }

    public void changePushNightYn(Boolean userPushNightYn) {
        this.userPushNightYn = userPushNightYn;
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

    public void withdrawalUser(){
        this.userUseYn = false;
    }

    @Builder
    public User(String userEmail, String password, String userName, String userNickname, String userPushKey, Boolean userPushYn, Boolean userPushNightYn, Boolean userUseYn, UserGrade userGrade, String userImage, String userBillingKey, String userSnsKind, String userSnsId) {
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
        this.userSnsKind = userSnsKind;
        this.userSnsId = userSnsId;
    }
}
