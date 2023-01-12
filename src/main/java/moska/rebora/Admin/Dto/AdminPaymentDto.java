package moska.rebora.Admin.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Dto.PaymentLogDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminPaymentDto {

    private String paymentId;
    private String paymentContent;
    private Integer paymentAmount;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private String paymentCardCode;
    private String pgProvider;
    private String paymentCardName;
    private LocalDateTime paidAt;
    private String paymentCardNumber;
    private String paymentMemo;
    private String receiptUrl;
    private String userEmail;
    private String userNickname;
    private String userName;
    private Long userId;
    private Long movieId;
    private String movieName;
    private Long recruitmentId;
    private List<PaymentLogDto> paymentLogDtoList;

    @QueryProjection
    public AdminPaymentDto(String paymentId, String paymentContent, Integer paymentAmount, String paymentMethod, PaymentStatus paymentStatus, String paymentCardCode, String pgProvider, String paymentCardName, LocalDateTime paidAt, String paymentCardNumber, String paymentMemo, String receiptUrl, String userEmail, String userNickname, String userName, Long userId, Long movieId, String movieName, Long recruitmentId) {
        this.paymentId = paymentId;
        this.paymentContent = paymentContent;
        this.paymentAmount = paymentAmount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentCardCode = paymentCardCode;
        this.pgProvider = pgProvider;
        this.paymentCardName = paymentCardName;
        this.paidAt = paidAt;
        this.paymentCardNumber = paymentCardNumber;
        this.paymentMemo = paymentMemo;
        this.receiptUrl = receiptUrl;
        this.userEmail = userEmail;
        this.userNickname = userNickname;
        this.userName = userName;
        this.userId = userId;
        this.movieId = movieId;
        this.movieName = movieName;
        this.recruitmentId = recruitmentId;
    }
}
