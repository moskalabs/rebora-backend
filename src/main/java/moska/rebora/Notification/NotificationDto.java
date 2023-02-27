package moska.rebora.Notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "알림 DTO")
public class NotificationDto {
    @Schema(description = "알림 ID")
    private Long notificationId;
    @Schema(description = "알림 제목")
    private String notificationSubject;
    @Schema(description = "알림 내용")
    private String notificationContent;
    @Schema(description = "알림 종류")
    private NotificationKind notificationKind;
    @Schema(description = "알림 읽음 여부")
    private Boolean notificationReadYn;
    @Schema(description = "모집 아이디")
    private Long recruitmentId;
    @Schema(description = "결제 상태")
    private PaymentStatus paymentStatus;
    @Schema(description = "결제 액수")
    private Integer paymentAmount;
    @Schema(description = "등록일")
    private LocalDateTime regDate;
    @Schema(description = "수정일")
    private LocalDateTime modDate;
    @Schema(description = "영화 이름")
    private String movieName;
}
