package moska.rebora.User.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.util.DuplicateElementException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationManager;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserLoginDto;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static moska.rebora.Common.CommonConst.NAVER_TOKEN_ME_URL;
import static moska.rebora.Common.CommonConst.NAVER_TOKEN_URL;

@Service
@Slf4j
@AllArgsConstructor
public class OathServiceImpl implements OathService {

    private RestTemplate restTemplate;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;
    private PasswordAuthAuthenticationManager authenticationManager;
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    private static final Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

    @Override
    public UserLoginDto login(String authToken, UserSnsKind userSnsKind) {

        HashMap<Object, Object> prmMap = new HashMap<Object, Object>();

        User user = new User();
        UserLoginDto userLoginDto = new UserLoginDto();
        String userSnsId = "";
        String userName = "";
        String userEmail = "";

        switch (userSnsKind) {
            case NAVER: {
                prmMap = getNaverUserInfo(authToken);
                userEmail = String.valueOf(prmMap.get("userEmail"));
                userName = String.valueOf(prmMap.get("userName"));
                userSnsId = String.valueOf(prmMap.get("snsId"));

                log.info("네이버 SNS 유저 아이디 결과 snsLoginUserEmail={} snsUserName={} snsUserSnsId={}", userEmail, userName, userSnsId);

                user = userRepository.getUserByUserEmail(userEmail);
                break;
            }
            case KAKAO: {
                prmMap = getKaKaoUserInfo(authToken);
                userEmail = String.valueOf(prmMap.get("user_email"));
                userSnsId = String.valueOf(prmMap.get("snsId"));
                log.info("카카오 SNS 유저 아이디 결과 snsLoginUserEmail={} snsUserSnsId={}", userEmail, userSnsId);
                user = userRepository.getUserByUserEmail(userEmail);
                break;
            }
        }

        return getUserLoginDto(user, userSnsKind, userSnsId);
    }

    @Override
    public UserLoginDto appleLogin(String userEmail, String clientId) {
        UserLoginDto userLoginDto = new UserLoginDto();
        User user = new User();
        if (userEmail == null) {
            Optional<User> userOptional = userRepository.getUserByUserSnsKindAndUserSnsId(UserSnsKind.APPLE, clientId);
            if (userOptional.isPresent()) {
                user = userOptional.get();
            }
        } else {
            user = userRepository.getUserByUserEmail(userEmail);
        }

        return getUserLoginDto(user, UserSnsKind.APPLE, clientId);
    }

    public UserLoginDto getUserLoginDto(User user, UserSnsKind userSnsKind, String userSnsId) {
        UserLoginDto userLoginDto = new UserLoginDto();

        if (user == null) {
            throw new NullPointerException("해당하는 유저가 없습니다. 회원가입 해주세요");
        } else if (user.getUserSnsKind() == null) {
            user.addUserSns(userSnsKind, userSnsId);
            userLoginDto = UserLoginDto
                    .builder()
                    .user(user)
                    .result(false)
                    .errorCode("409")
                    .message("이미 가입된 아이디가 있습니다 연동하시겠습니까?.")
                    .build();

        } else if (user.getUserSnsKind() != userSnsKind) {
            throw new DuplicateElementException("이미 가입된 SNS 아이디가 존재합니다. 다시 로그인 해주세요");
        } else {
            userLoginDto = UserLoginDto
                    .builder()
                    .user(user)
                    .result(true)
                    .token(createToken(user.getUserEmail()))
                    .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(user.getUserEmail()))
                    .build();
        }

        return userLoginDto;
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
        } catch (HttpClientErrorException e) {
            throw new JwtException("인증되지 않은 사용자입니다.");
        }
    }

    public HashMap<Object, Object> getKaKaoUserInfo(String accessToken) {

        try {
            HashMap<Object, Object> prmMap = new HashMap<Object, Object>();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(NAVER_TOKEN_ME_URL, HttpMethod.GET, request, String.class);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            JSONObject kakaoAccount = (JSONObject) jsonObj.get("kakao_account");
            JSONObject profile = (JSONObject) kakaoAccount.get("profile");

            prmMap.put("snsId", jsonObj.get("id"));
            prmMap.put("connectedAt", jsonObj.get("connected_at"));
            prmMap.put("user_name", profile.get("nickname"));
            prmMap.put("user_email", kakaoAccount.get("email"));

            return prmMap;
        } catch (ParseException e) {
            log.error(e.getMessage());
            return null;
        } catch (HttpClientErrorException e) {
            throw new JwtException("인증되지 않은 사용자입니다.");
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

    @Override
    public UserLoginDto syncUser(String userEmail, String userSnsKind, String userSnsId) {
        User user = userRepository.getUserByUserEmail(userEmail);
        user.addUserSns(UserSnsKind.valueOf(userSnsKind), userSnsId);
        userRepository.save(user);
        
        UserLoginDto userLoginDto = UserLoginDto.builder()
                .user(user)
                .result(true)
                .token(createToken(user.getUserEmail()))
                .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(user.getUserEmail()))
                .build();

        return userLoginDto;
    }

    /**
     * 토큰 발행
     *
     * @param userEmail 유저이메일
     * @return String
     */
    public String createToken(String userEmail) {

        PasswordAuthAuthenticationToken token = new PasswordAuthAuthenticationToken(userEmail, "");
        Authentication authentication = authenticationManager.authenticate(token);
        PasswordAuthAuthenticationToken authToken = (PasswordAuthAuthenticationToken) authentication;
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> claims = new HashMap<>();
        claims.put("id", authToken.getId().toString());
        claims.put("userName", authToken.getUserName());
        claims.put("role", authToken.getRole());
        claims.put("userEmail", authToken.getUserEmail());

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(authentication.getName(), String.valueOf(authentication.getAuthorities()), claims, expiredDate);

        return jwtAuthToken.getToken(jwtAuthToken);
    }
}
