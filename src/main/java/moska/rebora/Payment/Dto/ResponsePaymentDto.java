package moska.rebora.Payment.Dto;

import lombok.Data;

@Data
public class ResponsePaymentDto {

    private Integer amount;
    private String cardCode;
    private String cardName;
    private String cardNumber;
    private String customerUid;
    private Long paidAt;
    private String payMethod;
    private String pgProvider;
    private String receiptUrl;
}
