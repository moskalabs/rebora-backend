package moska.rebora.Payment.Service;

import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Payment.Dto.CardDto;
import moska.rebora.Payment.Dto.CustomerUIdDto;
import moska.rebora.Payment.Dto.MerchantUidDto;
import moska.rebora.Payment.Entity.Card;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;

public interface PaymentService {

//    void getBillingKey(String customerKey, String authKey, Long userId);

    String createCustomerUId(Long userId, Long recruitmentId);

    void applyCardUserRecruitment(UserRecruitment userRecruitment, String customerUid);

    void paymentByRecruitment(Recruitment recruitment);

    UserRecruitment paymentCancel(UserRecruitment userRecruitment);

    Integer CardUseCount(Long userId);

    CardDto getCard(Long userId);

    void deleteCard(Long userId);

    Payment getBatchPayment(User user, Recruitment recruitment, Payment payment, Theater theater, UserRecruitment userRecruitment, Movie movie);

    CustomerUIdDto createCustomerUid(Long userId, Long recruitmentId, Integer userRecruitmentPeople);

    void reserveRecruitment(Long userRecruitmentId);

    MerchantUidDto createMerchantUid(Long userId, Long RecruitmentId, Integer userRecruitmentPeople);

    void saveImmediatePayment(Long userRecruitmentId, String merchantUid, String impUid);

    String createPaymentId(Long userId, Long recruitmentId);

    JSONObject getPaymentByMerchantUid(String impUid);

    JSONObject refundPayment(String impUid);

    Payment createPayment(
            Integer amount,
            String cardNumber,
            String cardName,
            String cardCode,
            String paymentMethod,
            String pgProvider,
            String receiptUrl,
            String paymentContent,
            Boolean paymentReserve,
            UserRecruitment userRecruitment,
            String id,
            String impUid,
            PaymentStatus paymentStatus,
            LocalDateTime authenticatedAt
    );
}
