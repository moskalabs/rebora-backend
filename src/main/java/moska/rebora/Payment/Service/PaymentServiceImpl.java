package moska.rebora.Payment.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Util;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Dto.CardDto;
import moska.rebora.Payment.Dto.CustomerUIdDto;
import moska.rebora.Payment.Dto.MerchantUidDto;
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
import moska.rebora.User.Service.UserService;
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

    @Autowired
    UserService userService;

    /**
     * 결제 유저 아이디 생성
     *
     * @return String
     */
    @Override
    public String createCustomerUId(Long userId, Long recruitmentId) {

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return "rebora_" + userId + "_" + recruitmentId + "_" + now;
    }

    @Override
    public void applyCardUserRecruitment(UserRecruitment userRecruitment, String customerUid) {
        userRecruitmentRepository.save(userRecruitment);
    }

    @Override
    @Transactional
    public void paymentByRecruitment(Recruitment recruitment) {
        Theater theater = recruitment.getTheater();
        Movie movie = recruitment.getMovie();

        List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getUserRecruitmentByRecruitment(recruitment);

        for (UserRecruitment userRecruitment : userRecruitmentList) {

            //예약 고유번호
            String customerUId = userRecruitment.getCustomerUId();

            User user = userRecruitment.getUser();
            log.info(user.getUserNickname() + "님의 예약 카드 결제 시작");

            String paymentId = createPaymentId(user.getId(), recruitment.getId());
            String paymentName = user.getUserNickname() + "님의 상영 확정된 모집 영화(" + movie.getMovieName() + ")의 " + userRecruitment.getUserRecruitmentPeople() + "명 예약 결제";
            Integer theaterPrice = (theater.getTheaterPrice() + movie.getMoviePrice()) * userRecruitment.getUserRecruitmentPeople();

            if (customerUId.equals("")) {
                paymentName = "빌링키 정보가 없습니다.";
                Payment payment = Payment.builder()
                        .id(paymentId)
                        .userRecruitment(userRecruitment)
                        .paymentAmount(theaterPrice)
                        .paymentStatus(PaymentStatus.FAILURE)
                        .paymentMethod("card")
                        .paymentContent(paymentName)
                        .build();

                paymentRepository.save(payment);

                PaymentLog paymentLog = PaymentLog.builder()
                        .payment(payment)
                        .paymentLogAmount(theaterPrice)
                        .paymentLogStatus(PaymentStatus.FAILURE)
                        .paymentMethod("card")
                        .paymentLogContent(paymentName)
                        .build();

                paymentLogRepository.save(paymentLog);

            } else {

                log.info("customerUid={}, amount={} paymentId={} paymentName={}", customerUId, theaterPrice, paymentId, paymentName);

                JSONObject result = paymentAgain(customerUId, paymentId, theaterPrice, paymentName);
                Long code = (Long) result.get("code");
                String message = (String) result.get("message"); //에러 메세지
                JSONObject response = (JSONObject) result.get("response");

                Long amount = (Long) response.get("amount"); //결제금액
                Long paidAt = (Long) response.get("paid_at"); //결제 시각
                String cardNumber = (String) response.get("card_number"); //카드 번호
                String cardName = (String) response.get("card_name"); //카드사
                String cardCode = (String) response.get("card_code"); //카드 코드
                String payMethod = (String) response.get("pay_method"); //결제 방법
                String pgProvider = (String) response.get("pg_provider"); //PG사
                String receiptUrl = (String) response.get("receipt_url"); //영수증 URL
                String impUid = (String) response.get("impUid"); //영수증 URL
                LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAt), TimeZone.getDefault().toZoneId());

                if (code != 0) {

                    Payment payment = createPayment(Math.toIntExact(amount), cardNumber, cardName, cardCode, payMethod, pgProvider, receiptUrl, message, false, userRecruitment, paymentId, impUid, PaymentStatus.FAILURE, authenticatedAt);
                    notificationService.createPaymentEndNotification(recruitment, theater, user, movie, payment, false);

                    recruitment.minusRecruitmentPeople(userRecruitment.getUserRecruitmentPeople());
                    recruitmentRepository.save(recruitment);
                } else {
                    Payment payment = createPayment(Math.toIntExact(amount), cardNumber, cardName, cardCode, payMethod, pgProvider, receiptUrl, paymentName, false, userRecruitment, paymentId, impUid, PaymentStatus.COMPLETE, authenticatedAt);
                    notificationService.createPaymentEndNotification(recruitment, theater, user, movie, payment, true);
                }
                log.info(user.getUserNickname() + "님의 예약 카드 결제 종료");
            }
        }
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
                payment.updatePayment(paymentName, Math.toIntExact(amount), payMethod, PaymentStatus.COMPLETE, cardCode, pgProvider, cardName, authenticatedAt, receiptUrl, cardNumber, userRecruitment, "", true);
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
    public UserRecruitment paymentCancel(UserRecruitment userRecruitment) {

        userRecruitment.cancelUserRecruitment(false);
        userRecruitmentRepository.save(userRecruitment);

        Payment payment = userRecruitment.getPayment();
        if (payment != null) {

            JSONObject result = refundPayment(payment.getImpUid());
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

            createPayment(Math.toIntExact(amount), cardNumber, cardName, cardCode, payMethod, pgProvider, receiptUrl, "결제 취소", false, userRecruitment, payment.getId(), payment.getImpUid(), PaymentStatus.CANCEL, authenticatedAt);
        }

        return userRecruitment;
    }

    public JSONObject refundPayment(String impUid) {

        String paymentAuth = getPaymentAuth();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        params.put("imp_uid", impUid);

        try {

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

            return (JSONObject) jsonParser.parse(response.body());

        } catch (IOException | ParseException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer CardUseCount(Long userId) {

        User user = checkUser(userId);

        return cardRepository.countByCardUseYnAndAndUser(true, user);
    }

    @Override
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
                        userRecruitment,
                        "",
                        true
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

    /**
     * 빌링키 생성
     *
     * @param userId                유저 아이디
     * @param recruitmentId         모집 아이디
     * @param userRecruitmentPeople 신청 모집 인원
     * @return CustomerUIdDto
     */
    @Override
    public CustomerUIdDto createCustomerUid(Long userId, Long recruitmentId, Integer userRecruitmentPeople) {
        CustomerUIdDto customerUIdDto = new CustomerUIdDto();

        Optional<User> optionalUser = userRepository.findById(userId);

        //유저가 없는 경우
        if (optionalUser.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저입니다. 다시 시도해 주세요.");
        }

        //모집이 없는 경우
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(recruitmentId);

        if (optionalRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집입니다. 다시 시도해 주세요.");
        }

        User user = optionalUser.get(); //유저
        Recruitment recruitment = optionalRecruitment.get(); //모집
        Theater theater = recruitment.getTheater(); // 상영관

        //유저 유효성 검사
        if (!userService.isOnValidUser(user)) {
            throw new RuntimeException("모집 신청 불가능한 유저입니다. 다시 시도해 주세요.");
        }

        //최대 모집 여부
        if (recruitment.getRecruitmentPeople() + userRecruitmentPeople >= theater.getTheaterMaxPeople()) {
            throw new RuntimeException("모집 인원이 초과했습니다. 다시 시도해 주세요");
        }

        Optional<UserRecruitment> optionalUserRecruitment = userRecruitmentRepository.getUserRecruitmentByUserAndRecruitment(user, recruitment);
        String customerUId = createCustomerUId(userId, recruitmentId); //빌링 키

        //유저_모집이 없을 경우 생성
        if (optionalUserRecruitment.isEmpty()) {
            UserRecruitment userRecruitment = UserRecruitment
                    .builder()
                    .userRecruitmentYn(false)
                    .recruitment(recruitment)
                    .user(user)
                    .userRecruitmentPeople(userRecruitmentPeople)
                    .userRecruitmentWish(false)
                    .customerUId(customerUId)
                    .build();

            userRecruitmentRepository.save(userRecruitment);
            customerUIdDto.setCustomerUid(customerUId);
            customerUIdDto.setUserRecruitmentId(userRecruitment.getId());
        } else {
            UserRecruitment userRecruitment = optionalUserRecruitment.get();
            userRecruitment.applyCustomerUId(customerUId, false, userRecruitmentPeople);
            userRecruitmentRepository.save(userRecruitment);
            customerUIdDto.setCustomerUid(customerUId);
            customerUIdDto.setUserRecruitmentId(userRecruitment.getId());
        }

        return customerUIdDto;
    }

    /**
     * 모집 예약 등록
     *
     * @param userRecruitmentId 유저 모집 아이디
     */
    @Override
    public void reserveRecruitment(Long userRecruitmentId) {

        Optional<UserRecruitment> userRecruitmentOptional = userRecruitmentRepository.findById(userRecruitmentId);

        //유저 모집이 존재하지 않을 경우
        if (userRecruitmentOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저 모집입니다.");
        }

        UserRecruitment userRecruitment = userRecruitmentOptional.get();

        Recruitment recruitment = userRecruitment.getRecruitment();

        userRecruitment.updateUserRecruitmentYn(true);
        recruitment.plusRecruitmentPeople(userRecruitment.getUserRecruitmentPeople());

        userRecruitmentRepository.save(userRecruitment);
        recruitmentRepository.save(recruitment);
    }

    @Override
    public MerchantUidDto createMerchantUid(Long userId, Long recruitmentId, Integer userRecruitmentPeople) {

        Optional<User> optionalUser = userRepository.findById(userId);
        MerchantUidDto merchantUidDto = new MerchantUidDto();
        //유저가 없는 경우
        if (optionalUser.isEmpty()) {
            throw new NullPointerException("존재하지 않는 유저입니다. 다시 시도해 주세요.");
        }

        //모집이 없는 경우
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(recruitmentId);

        if (optionalRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집입니다. 다시 시도해 주세요.");
        }

        User user = optionalUser.get(); //유저
        Recruitment recruitment = optionalRecruitment.get(); //모집
        Theater theater = recruitment.getTheater(); // 상영관

        //유저 유효성 검사
        if (!userService.isOnValidUser(user)) {
            throw new RuntimeException("모집 신청 불가능한 유저입니다. 다시 시도해 주세요.");
        }

        //최대 모집 여부
        if (recruitment.getRecruitmentPeople() + userRecruitmentPeople >= theater.getTheaterMaxPeople()) {
            throw new RuntimeException("모집 인원이 초과했습니다. 다시 시도해 주세요");
        }

        Optional<UserRecruitment> optionalUserRecruitment = userRecruitmentRepository.getUserRecruitmentByUserAndRecruitment(user, recruitment);
        String merchantUid = createPaymentId(userId, recruitmentId);

        if (optionalUserRecruitment.isEmpty()) {

            UserRecruitment userRecruitment = UserRecruitment
                    .builder()
                    .userRecruitmentYn(false)
                    .recruitment(recruitment)
                    .user(user)
                    .userRecruitmentPeople(userRecruitmentPeople)
                    .userRecruitmentWish(false)
                    .build();

            userRecruitmentRepository.save(userRecruitment);
            merchantUidDto.setMerchantUid(merchantUid);
            merchantUidDto.setUserRecruitmentId(userRecruitment.getId());

        } else {

            UserRecruitment userRecruitment = optionalUserRecruitment.get();

            userRecruitment.applyCustomerUId("", false, userRecruitmentPeople);
            userRecruitmentRepository.save(userRecruitment);

            Payment payment = userRecruitment.getPayment();
            if (payment == null) {
                merchantUidDto.setMerchantUid(merchantUid);
            } else {
                merchantUidDto.setMerchantUid(payment.getId());
            }

            merchantUidDto.setUserRecruitmentId(userRecruitment.getId());
            merchantUidDto.setMerchantUid(merchantUid);
        }

        return merchantUidDto;
    }

    @Override
    @Transactional
    public void saveImmediatePayment(Long userRecruitmentId, String merchantUid, String impUid) {

        JSONObject response = new JSONObject();
        Optional<UserRecruitment> userRecruitmentOptional = userRecruitmentRepository.getUserRecruitmentById(userRecruitmentId);

        if (userRecruitmentOptional.isEmpty()) {
            throw new RuntimeException("존재 하지 않는 모집 신청입니다.");
        }

        UserRecruitment userRecruitment = userRecruitmentOptional.get();
        Recruitment recruitment = userRecruitment.getRecruitment();
        User user = userRecruitment.getUser();
        Movie movie = recruitment.getMovie();
        Theater theater = recruitment.getTheater();

        //결제 내용
        String content = user.getUserNickname() + "님의 상영 확정된 모집 영화(" + movie.getMovieName() + ")의 " + userRecruitment.getUserRecruitmentPeople() + "명 즉시 결제";

        //주문번호로 결제 정보 가져오기
        response = getPaymentByMerchantUid(impUid);

        Long amount = (Long) response.get("amount"); //결제금액
        Long paidAt = (Long) response.get("paid_at"); //결제 시각
        String cardNumber = (String) response.get("card_number"); //카드 번호
        String cardName = (String) response.get("card_name"); //카드사
        String cardCode = (String) response.get("card_code"); //카드 코드
        String payMethod = (String) response.get("pay_method"); //결제 방법
        String pgProvider = (String) response.get("pg_provider"); //PG사
        String receiptUrl = (String) response.get("receipt_url"); //영수증 URL

        //결제 시기
        LocalDateTime authenticatedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAt), TimeZone.getDefault().toZoneId());

        //결제 엔티티 생성
        Payment payment = createPayment(Math.toIntExact(amount), cardNumber, cardName, cardCode, payMethod, pgProvider, receiptUrl, content, false, userRecruitment, merchantUid, impUid, PaymentStatus.COMPLETE, authenticatedAt);

        //알림 생성
        notificationService.createNotificationPayment(
                "모집 참여가 완료되었습니다.",
                notificationService.createNotificationContent(movie.getMovieName(), theater.getTheaterStartDatetime(), theater.getTheaterDay(), theater.getTheaterCinemaBrandName(), theater.getTheaterCinemaName(), theater.getTheaterName()),
                NotificationKind.CONFORMATION,
                user,
                recruitment,
                payment
        );

        userRecruitment.cancelUserRecruitment(true);
        recruitment.plusRecruitmentPeople(userRecruitment.getUserRecruitmentPeople());

        recruitmentRepository.save(recruitment);
        userRecruitmentRepository.save(userRecruitment);
    }

    @Override
    public Payment createPayment(
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
    ) {
        LocalDateTime now = LocalDateTime.now();
        Optional<Payment> optionalPayment = paymentRepository.getPaymentByUserRecruitment(userRecruitment);

        //payment가 없을 시 생성
        if (optionalPayment.isEmpty()) {

            Payment payment = Payment
                    .builder()
                    .id(id)
                    .paymentReserve(paymentReserve)
                    .paymentMethod(paymentMethod)
                    .paymentMemo("")
                    .paymentStatus(paymentStatus)
                    .paymentContent(paymentContent)
                    .paymentAmount(amount)
                    .paymentCardNumber(cardNumber)
                    .paymentCardName(cardName)
                    .paymentCardCode(cardCode)
                    .pgProvider(pgProvider)
                    .receiptUrl(receiptUrl)
                    .impUid(impUid)
                    .paymentCardCode(cardCode)
                    .paidAt(authenticatedAt)
                    .userRecruitment(userRecruitment)
                    .build();

            paymentRepository.save(payment);

            createPaymentLog(
                    paymentContent,
                    amount,
                    paymentMethod,
                    paymentStatus,
                    cardCode,
                    pgProvider,
                    receiptUrl,
                    cardNumber,
                    cardName,
                    payment
            );

            userRecruitment.updatePayment(payment);
            userRecruitmentRepository.save(userRecruitment);

            return payment;
        } else {

            Payment payment = optionalPayment.get();

            payment.updatePayment(
                    paymentContent,
                    amount,
                    paymentMethod,
                    paymentStatus,
                    cardCode,
                    pgProvider,
                    cardName,
                    authenticatedAt,
                    receiptUrl,
                    cardNumber,
                    userRecruitment,
                    impUid,
                    paymentReserve
            );

            createPaymentLog(
                    paymentContent,
                    amount,
                    paymentMethod,
                    paymentStatus,
                    cardCode,
                    pgProvider,
                    receiptUrl,
                    cardNumber,
                    cardName,
                    payment
            );

            paymentRepository.save(payment);

            return payment;
        }
    }

    public void createPaymentLog(
            String paymentContent,
            Integer amount,
            String method,
            PaymentStatus paymentStatus,
            String cardCode,
            String pgProvider,
            String receiptUrl,
            String cardNumber,
            String cardName,
            Payment payment
    ) {
        PaymentLog paymentLog = PaymentLog
                .builder()
                .paymentLogContent(paymentContent)
                .paymentLogAmount(amount)
                .paymentMethod(method)
                .paymentLogStatus(paymentStatus)
                .paymentLogCardCode(cardCode)
                .pgProvider(pgProvider)
                .receiptUrl(receiptUrl)
                .paymentCardNumber(cardNumber)
                .paymentCardName(cardName)
                .payment(payment)
                .build();

        paymentLogRepository.save(paymentLog);
    }

    @Override
    public JSONObject getPaymentByMerchantUid(String impUid) {

        JSONObject jsonObject = new JSONObject();
        try {

            String paymentAuth = getPaymentAuth(); //결제 auth 가져오기
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = new HashMap<>();
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/payments/" + impUid))
                    .header("Authorization", "Bearer " + paymentAuth)
                    .header("Content-Type", "application/json")
                    .method("GET", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();

            JSONObject result = (JSONObject) jsonParser.parse(response.body());
            Long code = (Long) result.get("code");
            String message = (String) result.get("message"); //에러 메세지

            if (code != 0L) {
                throw new RuntimeException(message);
            }

            jsonObject = (JSONObject) result.get("response");

        } catch (IOException | ParseException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    public JSONObject paymentAgain(
            String customerUid,
            String merchantUid,
            Integer amount,
            String paymentName
    ) {

        JSONObject jsonObject = new JSONObject();
        try {

            String paymentAuth = getPaymentAuth(); //결제 auth 가져오기
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> params = new HashMap<>();
            params.put("customer_uid", customerUid);
            params.put("merchant_uid", merchantUid);
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

            jsonObject = (JSONObject) jsonParser.parse(response.body());

        } catch (IOException | InterruptedException | ParseException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }
}
