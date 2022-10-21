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

    @Column
    private String password;

    @Column(length = 50, nullable = false)
    private String user_name;

    @Column(length = 50, nullable = false, unique = true)
    private String user_nickname;

    @Column(length = 100)
    private String user_push_key;

    @Column(nullable = false)
    private Boolean user_push_yn;

    @Column(nullable = false)
    private Boolean user_use_yn;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserGrade user_grade;

    @Column
    private String user_image;

    @Column
    private String user_billing_key;

    @Column(length = 10)
    private String user_sns_kind;

    @Column
    private String user_sns_id;

    @Builder
    public User(String password, String user_name, String user_nickname, String user_push_key, Boolean user_push_yn, Boolean user_use_yn, UserGrade user_grade, String user_image, String user_billing_key, String user_sns_kind, String user_sns_id) {
        this.password = password;
        this.user_name = user_name;
        this.user_nickname = user_nickname;
        this.user_push_key = user_push_key;
        this.user_push_yn = user_push_yn;
        this.user_use_yn = user_use_yn;
        this.user_grade = user_grade;
        this.user_image = user_image;
        this.user_billing_key = user_billing_key;
        this.user_sns_kind = user_sns_kind;
        this.user_sns_id = user_sns_id;
    }
}
