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
    private String notification_subject;

    @Column
    private String notification_content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Notification(String notification_subject, String notification_content, User user) {
        this.notification_subject = notification_subject;
        this.notification_content = notification_content;
        this.user = user;
    }
}
