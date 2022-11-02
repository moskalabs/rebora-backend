package moska.rebora.Notification.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.User.Entity.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "notification_id", nullable = false)
    private Long id;

    @Column
    private String notificationSubject;

    @Column
    private String notificationContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notification(String notificationSubject, String notificationContent, User user) {
        this.notificationSubject = notificationSubject;
        this.notificationContent = notificationContent;
        this.user = user;
    }
}
