package moska.rebora.Notification.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    @Comment("알림 PK")
    private Long id;

    @Column
    @Comment("알림 제목")
    private String notificationSubject;

    @Column
    @Comment("알림 내용")
    private String notificationContent;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Comment("알림 종류 : CANCEL = 취소, RECRUITING = 모집중, CONFORMATION = 상영확정")
    private NotificationKind notificationKind;

    @Column(columnDefinition = "boolean default false")
    @Comment("알림 읽음 여부")
    private Boolean notificationReadYn;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    @Comment("결제 PK")
    private Payment payment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    @Comment("모집 PK")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void readNotification(){
        this.notificationReadYn = true;
    }

    @Builder
    public Notification(String notificationSubject, String notificationContent, NotificationKind notificationKind, Boolean notificationReadYn, Payment payment, Recruitment recruitment, User user) {
        this.notificationSubject = notificationSubject;
        this.notificationContent = notificationContent;
        this.notificationKind = notificationKind;
        this.notificationReadYn = notificationReadYn;
        this.payment = payment;
        this.recruitment = recruitment;
        this.user = user;
    }
}
