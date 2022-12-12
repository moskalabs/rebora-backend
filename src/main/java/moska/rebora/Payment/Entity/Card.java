package moska.rebora.Payment.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.User.Entity.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카드 아이디")
    @Column(name = "card_id", nullable = false)
    private Long id;

    @Column
    @Comment("인증 시각")
    private LocalDateTime authenticatedAt;

    @Column(length = 10)
    @Comment("카드 이름")
    private String cardName;

    @Column(length = 50)
    @Comment("카드번호")
    private String cardNumber;

    @Column(length = 10)
    @Comment("카드 코드")
    private String cardCode;

    @Column
    @Comment("카드 uid")
    private String customerUid;

    @Column
    @Comment("카드 사용여부")
    private Boolean cardUseYn;

    @Column
    private String pgProvider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void changeBeforeCardYn(Boolean cardUseYn) {
        this.cardUseYn = cardUseYn;
    }

    public void applyCard(String cardCode, String cardName, String cardNumber, LocalDateTime authenticatedAt, String pgProvider) {
        this.cardCode = cardCode;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.authenticatedAt = authenticatedAt;
        this.pgProvider = pgProvider;
        this.cardUseYn = true;
    }

    @Builder
    public Card(LocalDateTime authenticatedAt, String cardName, String cardNumber, String cardCode, String customerUid, Boolean cardUseYn, User user) {
        this.authenticatedAt = authenticatedAt;
        this.cardName = cardName;
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.customerUid = customerUid;
        this.cardUseYn = cardUseYn;
        this.user = user;
    }
}
