package moska.rebora.Payment.Service;

import moska.rebora.Common.BaseResponse;
import moska.rebora.Payment.Dto.CardDto;
import moska.rebora.Payment.Entity.Card;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;

public interface PaymentService {

//    void getBillingKey(String customerKey, String authKey, Long userId);

    String createCustomerUid(User user);

    void applyCard(User user, String customerUid);

    BaseResponse paymentConfirmMovie(User user, Recruitment recruitment, Integer userRecruitmentPeople);

    void paymentByRecruitment(Recruitment recruitment);

    BaseResponse paymentCancel(Payment payment);

    Integer CardUseCount(Long userId);

    CardDto getCard(Long userId);

    void deleteCard(Long userId);
}
