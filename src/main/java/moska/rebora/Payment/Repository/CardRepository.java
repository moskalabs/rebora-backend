package moska.rebora.Payment.Repository;

import moska.rebora.Payment.Entity.Card;
import moska.rebora.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> getCardsByCardUseYnAndUserId(Boolean cardUseYn, Long userId);

    Optional<Card> getCardByCardUseYnAndUser(Boolean cardUseYn, User user);

    Card getCardByCustomerUid(String customerUid);

    Integer countByCardUseYnAndAndUser(Boolean cardUseYn, User user);

    List<Card> getCardsByUser(User user);
}
