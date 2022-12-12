package moska.rebora.Payment.Dto;

import lombok.Data;

@Data
public class BillingKeyDto {

    private String mId;
    private String customerKey;
    private String authenticatedAt;
    private String method;
    private String billingKey;
    private CardDto cardDto;
}
