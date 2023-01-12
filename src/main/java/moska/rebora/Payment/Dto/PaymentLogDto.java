package moska.rebora.Payment.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moska.rebora.Enum.PaymentStatus;

import java.time.LocalDateTime;

@Data
public class PaymentLogDto {

    private Long paymentLogId;
    private String paymentLogContent;
    private Integer paymentLogAmount;
    private String paymentMethod;
    private PaymentStatus paymentLogStatus;
    private String paymentLogCardCode;
    private String pgProvider;
    private LocalDateTime paidAt;
    private String receiptUrl;
    private String paymentCardNumber;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
