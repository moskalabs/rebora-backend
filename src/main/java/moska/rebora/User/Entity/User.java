package moska.rebora.User.Entity;

import lombok.*;
import moska.rebora.Common.BaseTimeEntity;
import moska.rebora.Enum.UserGrade;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String userEmail;

    @Column
    private String password;

    @Column(length = 50, nullable = false)
    private String userName;

    @Column(length = 50, nullable = false, unique = true)
    private String userNickname;

    @Column(length = 100)
    private String userPushKey;

    @Column(nullable = false)
    private Boolean userPushYn;

    @Column(nullable = false)
    private Boolean userUseYn;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserGrade userGrade;

    @Column
    private String userImage;

    @Column
    private String userBillingKey;

    @Column(length = 10)
    private String userSnsKind;

    @Column
    private String userSnsId;

    @Builder
    public User(String userEmail, String password, String userName, String userNickname, String userPushKey, Boolean userPushYn, Boolean userUseYn, UserGrade userGrade, String userImage, String userBillingKey, String userSnsKind, String userSnsId) {
        this.userEmail = userEmail;
        this.password = password;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPushKey = userPushKey;
        this.userPushYn = userPushYn;
        this.userUseYn = userUseYn;
        this.userGrade = userGrade;
        this.userImage = userImage;
        this.userBillingKey = userBillingKey;
        this.userSnsKind = userSnsKind;
        this.userSnsId = userSnsId;
    }
}
