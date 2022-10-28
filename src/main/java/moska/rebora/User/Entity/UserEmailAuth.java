package moska.rebora.User.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseTimeEntity;
import moska.rebora.Enum.EmailAuthKind;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEmailAuth extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_email_auth_id", nullable = false)
    private Long id;

    @Column
    private String email;

    @Column
    private String verifyNumber;

    @Column
    private String authKey;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private EmailAuthKind emailAuthKind;

    @Column
    private LocalDateTime expireDate;

    @Column
    private Boolean expireYn;

    public void expireAuth(){
        this.expireYn = false;
    }

    public void updateAuthKey(String authKey){
        this.authKey = authKey;
        this.expireDate = LocalDateTime.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()));
    }

    public void sendEmailAuth(String email, String verifyNumber, Boolean expireYn, EmailAuthKind emailAuthKind){
        this.email = email;
        this.verifyNumber = verifyNumber;
        this.expireYn = expireYn;
        this.emailAuthKind = emailAuthKind;
    }

    @Builder
    public UserEmailAuth(String email, String verifyNumber, String authKey, LocalDateTime expireDate, Boolean expireYn, EmailAuthKind emailAuthKind) {
        this.email = email;
        this.verifyNumber = verifyNumber;
        this.authKey = authKey;
        this.expireDate = expireDate;
        this.expireYn = expireYn;
        this.emailAuthKind = emailAuthKind;
    }
}
