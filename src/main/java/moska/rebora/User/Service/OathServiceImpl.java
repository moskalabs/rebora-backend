package moska.rebora.User.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mchange.util.DuplicateElementException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationManager;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.User.DTO.ApplePublicKeyDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserLoginDto;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static moska.rebora.Common.CommonConst.*;

@Service
@Slf4j
@AllArgsConstructor
public class OathServiceImpl implements OathService {

    private RestTemplate restTemplate;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private PasswordAuthAuthenticationManager authenticationManager;
    private JwtAuthTokenProvider jwtAuthTokenProvider;
    PasswordEncoder passwordEncoder;

    private static final Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

    /**
     * SNS 로그인
     *
     * @param authToken   토큰
     * @param userSnsKind SNS 종류
     * @param userPushKey 유저 푸쉬 키
     * @return UserLoginDto
     */
    @Override
    public UserLoginDto login(String authToken, UserSnsKind userSnsKind, @Param("userPushKey") String userPushKey) {

        HashMap<Object, Object> prmMap = new HashMap<Object, Object>();
        UserLoginDto userLoginDto = new UserLoginDto();
        String userSnsId = "";
        String userEmail = "";

        //SNS 종류가 네이버 일 경우
        if (userSnsKind.equals(UserSnsKind.NAVER)) {
            log.info("네이버 로그인 Start");
            //토큰으로 유저 정보 가져오기
            prmMap = getNaverUserInfo(authToken);
            userEmail = String.valueOf(prmMap.get("userEmail"));
            userSnsId = String.valueOf(prmMap.get("snsId"));
            log.info("token={}", authToken);
            log.info("네이버 SNS 유저 아이디 결과 snsLoginUserEmail={} snsUserSnsId={}", userEmail, userSnsId);
            //login 데이터 만들기
            userLoginDto = snsLogin(userEmail, userSnsId, userPushKey, UserSnsKind.NAVER);
            log.info("네이버 로그인 End");
        }

        if (userSnsKind.equals(UserSnsKind.KAKAO)) {
            log.info("카카오 로그인 Start");
            //토큰으로 유저 정보 가져오기
            prmMap = getKaKaoUserInfo(authToken);
            userEmail = String.valueOf(prmMap.get("userEmail"));
            userSnsId = String.valueOf(prmMap.get("snsId"));
            //login 데이터 만들기
            log.info("카카오 SNS 유저 아이디 결과 snsLoginUserEmail={} snsUserSnsId={}", userEmail, userSnsId);
            userLoginDto = snsLogin(userEmail, userSnsId, userPushKey, UserSnsKind.KAKAO);
            log.info("카카오 로그인 End");
        }

        if (userSnsKind.equals(UserSnsKind.APPLE)) {
            log.info("애플 로그인 Start");
            log.info("token={}", authToken);
            //토큰으로 유저 정보 가져오기
            prmMap = getAppleUserInfo(authToken);
            userEmail = String.valueOf(prmMap.get("userEmail"));
            userSnsId = String.valueOf(prmMap.get("snsId"));
            log.info("애플 SNS 유저 아이디 결과 snsLoginUserEmail={} snsUserSnsId={}", userEmail, userSnsId);
            //login 데이터 만들기
            userLoginDto = snsLogin(userEmail, userSnsId, userPushKey, UserSnsKind.APPLE);
            log.info("애플 로그인 End");
        }
        return userLoginDto;
    }

    public UserLoginDto snsLogin(String userEmail, String userSnsId, String userPushKey, UserSnsKind userSnsKind) {

        UserLoginDto userLoginDto = new UserLoginDto();
        Optional<User> optionalUser = Optional.ofNullable(userRepository.getUserByUserEmail(userEmail));

        //유저 정보가 없을 경우 회원가입으로 안내
        if (optionalUser.isEmpty()) {
            userLoginDto.setResult(true);
            userLoginDto.setErrorCode("500");
            userLoginDto.setUserEmail(userEmail);
            userLoginDto.setUserSnsId(userSnsId);
            userLoginDto.setUserSnsKind(userSnsKind);
            userLoginDto.setMessage("유저정보가 존재하지 않습니다. 회원가입 하시겠습니까?");
        } else {
            User user = optionalUser.get();
            user.changePushKey(userPushKey);
            userRepository.save(user);
            userLoginDto = getUserLoginDto(user, userSnsKind);
        }

        return userLoginDto;
    }

    public HashMap<Object, Object> getAppleUserInfo(String idToken) {
        HashMap<Object, Object> prmMap = new HashMap<Object, Object>();
        ApplePublicKeyDto applePublicKeyDto = new ApplePublicKeyDto();
        String[] decodeArray = idToken.split("\\.");
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));
        JSONParser jsonParser = new JSONParser();
        try {

            JSONObject appleJson = (JSONObject) jsonParser.parse(header);
            log.info("appleJson={}", appleJson);
            List<ApplePublicKeyDto> applePublicKeyDtoList = getPublicAppleKeys();
            for (ApplePublicKeyDto publicKeyDto : applePublicKeyDtoList) {
                String appleKid = appleJson.get("kid").toString();
                if (appleKid.equals(publicKeyDto.getKid())) {
                    applePublicKeyDto = publicKeyDto;
                    break;
                }
            }

            Claims userInfo = Jwts.parser().setSigningKey(getPublicKey(applePublicKeyDto)).parseClaimsJws(idToken).getBody();
            log.info("jwts gogo={}", userInfo);
            String id = userInfo.get("sub").toString();
            String email = userInfo.get("email").toString();
            prmMap.put("userEmail", email);
            prmMap.put("snsId", id);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return prmMap;
    }

    public UserLoginDto getUserLoginDto(User user, UserSnsKind userSnsKind) {

        //일반 유저인 경우
        if (user.getUserSnsKind() == null) {
            throw new DuplicateElementException("SNS로 가입하지 않은 일반 유저입니다.");
            //SNS 종류가 다를 경우
        } else if (user.getUserSnsKind() != userSnsKind) {
            throw new DuplicateElementException("이미 가입된 SNS 아이디가 존재합니다. 다시 로그인 해주세요. 가입된 SNS : " + user.getUserSnsKind());
        } else {

            return UserLoginDto.builder()
                    .user(user)
                    .result(true)
                    .token(createToken(user.getUserEmail(), userSnsKind.name()))
                    .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(user.getUserEmail())).build();
        }
    }

    public HashMap<Object, Object> getNaverUserInfo(String accessToken) {

        try {
            HashMap<Object, Object> prmMap = new HashMap<Object, Object>();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(NAVER_TOKEN_ME_URL, HttpMethod.GET, request, String.class);
            log.info("body={}", response.getBody());
            log.info("statusCode={}", response.getStatusCode());
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject naverAccount = (JSONObject) jsonObj.get("response");
            prmMap.put("snsId", naverAccount.get("id"));
            prmMap.put("userEmail", naverAccount.get("email"));
            prmMap.put("userName", naverAccount.get("name"));
            return prmMap;
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public PublicKey getPublicKey(ApplePublicKeyDto applePublicKeyDto) {
        try {

            byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKeyDto.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKeyDto.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ApplePublicKeyDto> getPublicAppleKeys() {
        List<ApplePublicKeyDto> applePublicKeyDtoList = new ArrayList<>();
        try {
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange("https://appleid.apple.com/auth/keys", HttpMethod.GET, request, String.class);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONArray jsonArray = (JSONArray) jsonObj.get("keys");
            log.info("response={}", jsonObj);
            for (Object o : jsonArray) {
                ApplePublicKeyDto applePublicKeyDto = new ApplePublicKeyDto((JSONObject) o);
                applePublicKeyDtoList.add(applePublicKeyDto);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("네이버 로그인에 실패했습니다. 다시 시도해주세요");
        }
        return applePublicKeyDtoList;
    }

    public HashMap<Object, Object> getKaKaoUserInfo(String accessToken) {

        try {
            HashMap<Object, Object> prmMap = new HashMap<Object, Object>();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(KAKAO_TOKEN_ME_URL, HttpMethod.GET, request, String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject kakaoAccount = (JSONObject) jsonObj.get("kakao_account");

            prmMap.put("snsId", jsonObj.get("id"));
            prmMap.put("connectedAt", jsonObj.get("connected_at"));
            prmMap.put("userEmail", kakaoAccount.get("email"));

            return prmMap;
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("카카오 로그인에 실패했습니다. 다시 시도해주세요");
        }
    }

    @Override
    public HashMap<Object, Object> getUserInfo(UserSnsKind userSnsKind, String authToken) {

        HashMap<Object, Object> prmMap = new HashMap<Object, Object>();

        switch (userSnsKind) {
            case NAVER:
                prmMap = getNaverUserInfo(authToken);
                break;
            case KAKAO:
                prmMap = getKaKaoUserInfo(authToken);
                break;
        }

        return prmMap;
    }

    public String createToken(String userEmail, String password) {

        PasswordAuthAuthenticationToken token = new PasswordAuthAuthenticationToken(userEmail, password);
        Authentication authentication = authenticationManager.authenticate(token);
        PasswordAuthAuthenticationToken authToken = (PasswordAuthAuthenticationToken) authentication;
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> claims = new HashMap<>();
        claims.put("id", authToken.getId().toString());
        claims.put("userName", authToken.getUserName());
        claims.put("role", authToken.getRole());
        claims.put("userEmail", authToken.getUserEmail());

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(
                authentication.getName(),
                String.valueOf(authentication.getAuthorities()),
                claims,
                expiredDate
        );

        return jwtAuthToken.getToken(jwtAuthToken);
    }

    @Override
    @Transactional
    public UserLoginDto signUpSns(
            String userEmail,
            String userName,
            String userNickname,
            UserSnsKind userSnsKind,
            String userSnsId,
            Boolean userPushYn,
            Boolean userPushNightYn,
            String userPushKey
    ) {

        String bcryptPassword = passwordEncoder.encode(userSnsKind.name());

        User user = User
                .builder()
                .userUseYn(true)
                .password(bcryptPassword)
                .userGrade(UserGrade.NORMAL)
                .userPushYn(userPushYn)
                .userPushNightYn(userPushNightYn)
                .userNickname(userNickname)
                .userName(userName)
                .userSnsId(userSnsId)
                .userEmail(userEmail)
                .userSnsKind(userSnsKind)
                .userPushKey(userPushKey)
                .build();

        userRepository.save(user);

        return UserLoginDto.builder()
                .token(createToken(userEmail, userSnsKind.name()))
                .result(true)
                .user(user)
                .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(userEmail))
                .build();
    }
}
