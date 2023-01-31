package moska.rebora.Notification;

import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationDto {

    private Long notificationId;
    private String notificationSubject;
    private String notificationContent;
    private NotificationKind notificationKind;
    private Boolean notificationReadYn;
    private Long recruitmentId;
    private PaymentStatus paymentStatus;
    private Integer paymentAmount;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String movieName;
}
