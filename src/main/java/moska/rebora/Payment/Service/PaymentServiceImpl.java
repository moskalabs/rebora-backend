package moska.rebora.Payment.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Util;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Dto.CardDto;
import moska.rebora.Payment.Entity.Card;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Entity.PaymentLog;
import moska.rebora.Payment.Repository.CardRepository;
import moska.rebora.Payment.Repository.PaymentLogRepository;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${import.auth.imp_key}")
    private String ImpKey;

    @Value("${import.auth.imp_secret}")
    private String ImpSecret;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentLogRepository paymentLogRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    NotificationService notificationService;

    @Autowired
    Util util;

    @Override
    public String createCustomerUid(User user) {

        String customerUid = "rebora_" + user.getId() + "_" + util.createRandomString(12);

        Card card = Card
                .builder()
                .customerUid(customerUid)
                .cardUseYn(false)
                .user(user)
                .build();

        cardRepository.save(card);
        return customerUid;
    }

    @Override
    public void applyCard(User user, String customerUid) {

        try {
            String paymentAuth = getPaymentAuth();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = new HashMap<>();
            params.put("customer_uid", customerUid);
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/subscribe/customers/" + customerUid))
                    .header("Authorization", "Bearer " + paymentAuth)
                    .header("Content-Type", "application/json; charset=euc-kr")
                    .method("GET", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.OK.value()) {
                log.info("response={}", response.body());
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObj = (JSONObject) jsonParser.parse(response.body());
                JSONObject responseJson = (JSONObject) jsonObj.get("response");

                List<Card> cardList = cardRepository.getCardsByCardUseYnAndUserId(true, user.getId());

                if (!cardList.isEmpty()) {
                    cardList.forEach(s -> s.changeBeforeCardYn(false));
                    cardRepository.saveAll(cardList);
                }

                long inserted = (long) responseJson.get("inserted");
                LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(inserted), TimeZone.getDefault().toZoneId());
                Card card = cardRepository.getCardByCustomerUid(customerUid);
                card.applyCard(
                        String.valueOf(responseJson.get("card_code")),
                        String.valueOf(responseJson.get("card_name")),
                        String.valueOf(responseJson.get("card_number")),
                        authenticatedAt,
                        String.valueOf(responseJson.get("pg_provider"))
                );

                cardRepository.save(card);
            } else {
                throw new NullPointerException("카드정보가 일치하지 않습니다. 다시시도해 주세요");
            }

        } catch (ParseException | IOException | InterruptedException e) {
            throw new RuntimeException("카드 정보를 가져오던 도중 오류가 발생했습니다 다시 시도해 주세요");
        }
    }

    @Override
    @Transactional
    public void paymentByRecruitment(Recruitment recruitment) {

        Theater theater = recruitment.getTheater();
        Movie movie = recruitment.getMovie();
        List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getUserRecruitmentByRecruitment(recruitment);
        for (UserRecruitment userRecruitment : userRecruitmentList) {
            BaseResponse baseResponse = new BaseResponse();
            User user = userRecruitment.getUser();
            Optional<Card> cardOptional = cardRepository.getCardByCardUseYnAndUser(true, user);

            String paymentId = createPaymentId(user.getId(), recruitment.getId());
            String paymentName = user.getUserNickname() + "님의 상영 확정된 모집 영화(" + movie.getMovieName() + ")의 " + userRecruitment.getUserRecruitmentPeople() + "명 예약 결제";

            if (cardOptional.isEmpty()) {
                throw new RuntimeException("카드 정보가 없습니다.");
            }

            Card card = cardOptional.get();
            log.info(user.getUserNickname() + "님의 예약 카드 결제 시작");
            log.info("customerUid={}, amount={} paymentId={} paymentName={}", card.getCustomerUid(), theater.getTheaterPrice() * userRecruitment.getUserRecruitmentPeople(), paymentId, paymentName);
            baseResponse = payProducts(card.getCustomerUid(), paymentId, theater.getTheaterPrice() * userRecruitment.getUserRecruitmentPeople(), paymentName, userRecruitment, true);
            notificationService.createPaymentEndNotification(recruitment, theater, user, movie, baseResponse.getResult());
            log.info(user.getUserNickname() + "님의 예약 카드 결제 종료");
        }
    }

    @Override
    @Transactional
    public BaseResponse paymentConfirmMovie(User user, Recruitment recruitment, Integer userRecruitmentPeople) {

        BaseResponse baseResponse = new BaseResponse();
        Theater theater = recruitment.getTheater();
        Movie movie = recruitment.getMovie();
        Optional<Card> cardOptional = cardRepository.getCardByCardUseYnAndUser(true, user);

        String paymentId = createPaymentId(user.getId(), recruitment.getId());
        String paymentName = user.getUserNickname() + "님의 상영 확정된 모집 영화(" + movie.getMovieName() + ")의 " + userRecruitmentPeople + "명 결제";

        Optional<UserRecruitment> userRecruitmentOptional = userRecruitmentRepository.getUserRecruitmentByUserAndRecruitment(user, recruitment);

        if (theater.getTheaterMaxPeople() < recruitment.getRecruitmentPeople() + userRecruitmentPeople) {
            throw new RuntimeException("최대 모집인원을 초과했습니다.");
        }

        if (cardOptional.isEmpty()) {
            throw new RuntimeException("카드 정보가 없습니다. 카드를 등록해주세요");
        }

//        if (recruitment.getRecruitmentStatus() == RecruitmentStatus.CANCEL || recruitment.getRecruitmentStatus() == RecruitmentStatus.RECRUITING || recruitment.getRecruitmentStatus() == RecruitmentStatus.COMPLETED) {
//            throw new RuntimeException("상영 확정된 모집만 추후 참여 가능합니다.");
//        }

        Card card = cardOptional.get();

        if (userRecruitmentOptional.isPresent()) {
            UserRecruitment userRecruitment = userRecruitmentOptional.get();
            log.info("확정된 영화 카드 결제 시작");
            log.info("customerUid={}, amount={} paymentId={} paymentName={}", card.getCustomerUid(), theater.getTheaterPrice() * userRecruitmentPeople, paymentId, paymentName);
            baseResponse = payProducts(card.getCustomerUid(), paymentId, theater.getTheaterPrice() * userRecruitmentPeople, paymentName, userRecruitment, false);
        } else {
            UserRecruitment userRecruitment = UserRecruitment
                    .builder()
                    .user(user)
                    .recruitment(recruitment)
                    .userRecruitmentPeople(userRecruitmentPeople)
                    .userRecruitmentYn(false)
                    .userRecruitmentWish(false)
                    .build();

            userRecruitmentRepository.save(userRecruitment);
            log.info("확정된 영화 카드 결제 시작");
            log.info("customerUid={}, amount={} paymentId={} paymentName={}", card.getCustomerUid(), theater.getTheaterPrice() * userRecruitmentPeople, paymentId, paymentName);
            baseResponse = payProducts(card.getCustomerUid(), paymentId, theater.getTheaterPrice() * userRecruitmentPeople, paymentName, userRecruitment, false);
        }

        if (baseResponse.getResult()) {
            recruitment.plusRecruitmentPeople(userRecruitmentPeople);
            recruitmentRepository.save(recruitment);
        }

        return baseResponse;
    }

    public String getPaymentAuth() {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> params = new HashMap<>();
            params.put("imp_key", ImpKey);
            params.put("imp_secret", ImpSecret);
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/users/getToken"))
                    .header("Content-Type", "application/json")
                    .method("POST", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.body());
            JSONObject responseJson = (JSONObject) jsonObj.get("response");
            return String.valueOf(responseJson.get("access_token"));
        } catch (ParseException | IOException | InterruptedException e) {
            throw new RuntimeException("카드 정보를 가져오던 도중 오류가 발생했습니다 다시 시도해 주세요");
        }
    }

    public BaseResponse payProducts(
            @Param("customerUid") String customerUid,
            @Param("paymentId") String paymentId,
            @Param("amount") Integer amount,
            @Param("paymentName") String paymentName,
            @Param("userRecruitment") UserRecruitment userRecruitment,
            @Param("paymentReserve") Boolean paymentReserve
    ) {
        try {
            BaseResponse baseResponse = new BaseResponse();

            String paymentAuth = getPaymentAuth();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = new HashMap<>();
            params.put("customer_uid", customerUid);
            params.put("merchant_uid", paymentId);
            params.put("amount", amount);
            params.put("name", paymentName);
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/subscribe/payments/again"))
                    .header("Authorization", "Bearer " + paymentAuth)
                    .header("Content-Type", "application/json")
                    .method("POST", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(response.body());
            Long code = (Long) result.get("code");
            if (code == 0L) {
                log.info(paymentName + "의 결제성공");
                createPayment(userRecruitment, result, paymentId, paymentName, true, paymentReserve);
                log.info("message={}", result.get("message"));
                baseResponse.setResult(true);
            } else {
                log.info(paymentName + "의 결제실패");
                String message = (String) result.get("message"); //에러 메세지
                createPayment(userRecruitment, result, paymentId, message, false, paymentReserve);
                baseResponse.setResult(false);
                baseResponse.setMessage((String) result.get("message"));
            }
            return baseResponse;

        } catch (IOException | InterruptedException | ParseException e) {
            throw new RuntimeException(e);
        }
    }


    void createPayment(
            UserRecruitment userRecruitment,
            JSONObject result,
            String paymentId,
            String paymentName,
            Boolean payResultSuccess,
            Boolean paymentReserve

    ) throws JsonProcessingException {
        PaymentStatus paymentStatusResult = payResultSuccess ? PaymentStatus.COMPLETE : PaymentStatus.FAILURE; //결제 상태
        Optional<Payment> paymentOptional = Optional.ofNullable(userRecruitment.getPayment());

        //결제 실패/성공
        if (!payResultSuccess) {
            userRecruitment.cancelUserRecruitment(false);

            //결제 내역 유무
            if (paymentOptional.isPresent()) {
                Payment payment = paymentOptional.get();
                payment.failPayment(paymentName, userRecruitment);
                paymentRepository.save(payment);
                PaymentLog paymentLog = PaymentLog
                        .builder()
                        .payment(payment)
                        .paymentLogContent(paymentName)
                        .paymentLogStatus(paymentStatusResult)
                        .build();
                paymentLogRepository.save(paymentLog);
            } else {
                Payment payment = Payment
                        .builder()
                        .id(paymentId)
                        .paymentContent(paymentName)
                        .paymentStatus(paymentStatusResult)
                        .paymentReserve(paymentReserve)
                        .userRecruitment(userRecruitment)
                        .build();

                PaymentLog paymentLog = PaymentLog
                        .builder()
                        .payment(payment)
                        .paymentLogContent(paymentName)
                        .paymentLogStatus(paymentStatusResult)
                        .build();

                paymentRepository.save(payment);
                userRecruitment.updatePayment(payment);
                paymentLogRepository.save(paymentLog);
            }
            userRecruitmentRepository.save(userRecruitment);

            //결제 성공 시
        } else {
            userRecruitment.cancelUserRecruitment(true);

            JSONObject response = (JSONObject) result.get("response"); //카드 결과 응답 값
            Long amount = (Long) response.get("amount"); //결제금액
            Long paidAt = (Long) response.get("paid_at"); //결제 시각
            String cardNumber = (String) response.get("card_number"); //카드 번호
            String cardName = (String) response.get("card_name"); //카드사
            String cardCode = (String) response.get("card_code"); //카드 코드
            String payMethod = (String) response.get("pay_method"); //결제 방법
            String pgProvider = (String) response.get("pg_provider"); //PG사
            String receiptUrl = (String) response.get("receipt_url"); //영수증 URL
            LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAt), TimeZone.getDefault().toZoneId());

            //결제 내역이 유무
            if (paymentOptional.isPresent()) {

                Payment payment = paymentOptional.get();
                payment.updatePayment(paymentName, Math.toIntExact(amount), payMethod, PaymentStatus.COMPLETE, cardCode, pgProvider, cardName, authenticatedAt, receiptUrl, cardNumber, userRecruitment);
                paymentRepository.save(payment);

                PaymentLog paymentLog = PaymentLog
                        .builder()
                        .payment(payment)
                        .paymentCardNumber(cardNumber)
                        .paymentLogAmount(Math.toIntExact(amount))
                        .paymentLogCardCode(cardCode)
                        .paymentLogContent(paymentName)
                        .paymentLogStatus(paymentStatusResult)
                        .paymentMethod(payMethod)
                        .paidAt(authenticatedAt)
                        .pgProvider(pgProvider)
                        .receiptUrl(receiptUrl)
                        .build();

                paymentLogRepository.save(paymentLog);

            } else {

                Payment payment = Payment
                        .builder()
                        .id(paymentId)
                        .paymentAmount(Math.toIntExact(amount))
                        .paymentCardNumber(cardNumber)
                        .paymentContent(paymentName)
                        .paymentStatus(paymentStatusResult)
                        .paymentMethod(payMethod)
                        .pgProvider(pgProvider)
                        .paidAt(authenticatedAt)
                        .paymentCardName(cardName)
                        .paymentReserve(paymentReserve)
                        .paymentCardCode(cardCode)
                        .receiptUrl(receiptUrl)
                        .userRecruitment(userRecruitment)
                        .build();

                PaymentLog paymentLog = PaymentLog
                        .builder()
                        .payment(payment)
                        .paymentCardNumber(cardNumber)
                        .paymentLogAmount(Math.toIntExact(amount))
                        .paymentLogCardCode(cardCode)
                        .paymentLogContent(paymentName)
                        .paymentLogStatus(paymentStatusResult)
                        .paymentMethod(payMethod)
                        .paidAt(authenticatedAt)
                        .pgProvider(pgProvider)
                        .receiptUrl(receiptUrl)
                        .build();

                paymentRepository.save(payment);
                paymentLogRepository.save(paymentLog);

                userRecruitment.updatePayment(payment);
            }
            userRecruitmentRepository.save(userRecruitment);
        }
    }

    @Override
    @Transactional
    public BaseResponse paymentCancel(Payment payment) {

        try {
            BaseResponse baseResponse = new BaseResponse();
            String paymentAuth = getPaymentAuth();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = new HashMap<>();
            params.put("merchant_uid", payment.getId());
            params.put("amount", payment.getPaymentAmount());
            params.put("checksum", payment.getPaymentAmount());
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/payments/cancel"))
                    .header("Authorization", "Bearer " + paymentAuth)
                    .header("Content-Type", "application/json")
                    .method("POST", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(response.body());
            log.info("result={}", result);
            Long code = (Long) result.get("code");
            if (code == 0L) {
                log.info("환불 성공");
                baseResponse.setResult(true);
                createRefund(payment, result, true);
                log.info("환불 종료");
            } else {
                log.info("환불 실패");
                baseResponse.setResult(false);
                baseResponse.setMessage((String) result.get("message"));
                createRefund(payment, result, false);
                log.info("환불 종료");
            }

            return baseResponse;

        } catch (IOException | InterruptedException | ParseException e) {
            throw new RuntimeException("결제 도중 오류가 발생했습니다. 다시 시도해주세요");

        }
    }

    public void createRefund(Payment payment, JSONObject result, Boolean refundResultSuccess) {

        JSONObject response = (JSONObject) result.get("response"); //카드 결과 응답 값
        if (refundResultSuccess) {
            Long cancelAmount = (Long) response.get("cancel_amount"); //결제금액
            String name = (String) response.get("name"); //결제 이름
            String cardNumber = (String) response.get("card_number"); //카드번호
            String payMethod = (String) response.get("pay_method"); //결제 방법
            String cardCode = (String) response.get("card_code"); //카드 코드
            Long paidAt = (Long) response.get("paid_at"); //결제 시각
            String pgProvider = (String) response.get("pg_provider"); //PG사
            JSONArray jsonArray = (JSONArray) response.get("cancel_receipt_urls");

            LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAt), TimeZone.getDefault().toZoneId());

            PaymentLog paymentLog = PaymentLog
                    .builder()
                    .paymentLogAmount(Math.toIntExact(cancelAmount))
                    .paymentLogContent(name + "의 취소요청")
                    .paymentCardNumber(cardNumber)
                    .paymentMethod(payMethod)
                    .paymentLogCardCode(cardCode)
                    .pgProvider(pgProvider)
                    .payment(payment)
                    .receiptUrl((String) jsonArray.get(0))
                    .paidAt(authenticatedAt)
                    .build();

            paymentLog.updatePaymentLogStatus(PaymentStatus.CANCEL);
            payment.updatePaymentStatus(PaymentStatus.CANCEL);
            paymentRepository.save(payment);
            paymentLogRepository.save(paymentLog);

        } else {
            PaymentLog paymentLog = PaymentLog
                    .builder()
                    .paymentLogContent((String) result.get("message"))
                    .paymentLogStatus(PaymentStatus.FAILURE)
                    .payment(payment)
                    .build();

            paymentLogRepository.save(paymentLog);
        }
    }

    @Override
    public Integer CardUseCount(Long userId) {
        User user = checkUser(userId);

        return cardRepository.countByCardUseYnAndAndUser(true, user);
    }

    public String createPaymentId(Long userId, Long recruitmentId) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(userId);
        stringBuilder.append("_");
        stringBuilder.append(recruitmentId);
        stringBuilder.append("_");
        stringBuilder.append(now);

        return stringBuilder.toString();
    }

    @Override
    public CardDto getCard(Long userId) {
        CardDto cardDto = new CardDto();
        User user = checkUser(userId);

        if (cardRepository.countByCardUseYnAndAndUser(true, user) > 0) {
            Optional<Card> cardOptional = cardRepository.getCardByCardUseYnAndUser(true, user);
            cardDto = new CardDto(cardOptional.get());
        }

        return cardDto;
    }

    @Override
    public void deleteCard(Long userId) {
        User user = checkUser(userId);
        List<Card> cardList = cardRepository.getCardsByUser(user);
        cardList.forEach(card -> card.changeBeforeCardYn(false));
        cardRepository.saveAll(cardList);
    }

    public User checkUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저입니다.");
        }

        return userOptional.get();
    }

    @Override
    @Transactional
    public Payment getBatchPayment(User user, Recruitment recruitment, Payment payment, Theater theater, UserRecruitment userRecruitment, Movie movie) {

        try {

            CardDto card = getCard(user.getId());

            String paymentAuth = getPaymentAuth();
            Integer amount = userRecruitment.getUserRecruitmentPeople() * theater.getTheaterPrice();
            String paymentName = user.getUserNickname() + "님의 상영 확정된 모집 영화(" + movie.getMovieName() + ")의 " + userRecruitment.getUserRecruitmentPeople() + "명 결제";

            log.info("확정된 영화 카드 결제 시작");
            log.info("customerUid={}, amount={} paymentId={} paymentName={}", card.getCustomerUid(), amount, payment.getId(), paymentName);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = new HashMap<>();
            params.put("customer_uid", card.getCustomerUid());
            params.put("merchant_uid", payment.getId());
            params.put("amount", amount);
            params.put("name", paymentName);

            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/subscribe/payments/again"))
                    .header("Authorization", "Bearer " + paymentAuth)
                    .header("Content-Type", "application/json")
                    .method("POST", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(response.body());
            Long code = (Long) result.get("code");
            String message = (String) result.get("message"); //에러 메세지
            if (code == 0L) {
                log.info(paymentName + "의 결제성공");
                JSONObject responseJson = (JSONObject) result.get("response"); //카드 결과 응답 값
                Long amountResult = (Long) responseJson.get("amount"); //결제금액
                Long paidAt = (Long) responseJson.get("paid_at"); //결제 시각
                String cardNumber = (String) responseJson.get("card_number"); //카드 번호
                String cardName = (String) responseJson.get("card_name"); //카드사
                String cardCode = (String) responseJson.get("card_code"); //카드 코드
                String payMethod = (String) responseJson.get("pay_method"); //결제 방법
                String pgProvider = (String) responseJson.get("pg_provider"); //PG사
                String receiptUrl = (String) responseJson.get("receipt_url"); //영수증 URL
                LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAt), TimeZone.getDefault().toZoneId());

                payment.updatePayment(
                        paymentName,
                        Math.toIntExact(amountResult),
                        payMethod,
                        PaymentStatus.COMPLETE,
                        cardCode,
                        pgProvider,
                        cardName,
                        authenticatedAt,
                        receiptUrl,
                        cardNumber,
                        userRecruitment
                );

                log.info("message={}", result.get("message"));
            } else {
                log.info(paymentName + "의 결제실패");
                payment.failPayment(message, userRecruitment);
            }

            return payment;
        } catch (IOException | InterruptedException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
