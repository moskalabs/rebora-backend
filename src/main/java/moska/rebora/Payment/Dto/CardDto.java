package moska.rebora.Payment.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import moska.rebora.Payment.Entity.Card;
import moska.rebora.User.Entity.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardDto {
    private Long cardId;
    private LocalDateTime authenticatedAt;
    private String cardName;
    private String cardNumber;
    private String cardCode;
    private String customerUid;
    private Boolean cardUseYn;
    private String pgProvider;

    public CardDto(Card card) {
        this.cardId = card.getId();
        this.authenticatedAt = card.getAuthenticatedAt();
        this.cardName = card.getCardName();
        this.cardNumber = card.getCardNumber();
        this.cardCode = card.getCardCode();
        this.customerUid = card.getCustomerUid();
        this.cardUseYn = card.getCardUseYn();
        this.pgProvider = card.getPgProvider();
    }

    public CardDto() {
    }
}
