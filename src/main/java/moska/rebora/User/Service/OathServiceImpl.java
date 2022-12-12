package moska.rebora.User.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import moska.rebora.User.DTO.AuthorizationNaverDto;
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
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static moska.rebora.Common.CommonConst.NAVER_TOKEN_ME_URL;
import static moska.rebora.Common.CommonConst.NAVER_TOKEN_URL;

@Service
@Slf4j
public class OathServiceImpl implements OathService {

    @Value("${sns.login.naver.key}")
    private String NAVER_KEY;

    @Value("${sns.login.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    private PasswordAuthAuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    private static final Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

    @Override
    public UserLoginDto login(
            String code,
            String callbackUrl,
            String state,
            UserSnsKind userSnsKind) {

        HashMap<Object, Object> prmMap = new HashMap<Object, Object>();

        if (userSnsKind.equals(UserSnsKind.NAVER)) {
            AuthorizationNaverDto authorizationNaverDto = naverLogin(code, callbackUrl, state);
            prmMap = getNaverUserInfo(authorizationNaverDto.getAccess_token());
            String userEmail = String.valueOf(prmMap.get("userEmail"));
            String userName = String.valueOf(prmMap.get("userName"));
            String userSnsId = String.valueOf(prmMap.get("snsId"));
            log.error("snsLoginUserEmail={}", userEmail);
            log.error("snsUserName={}", userName);
            log.error("snsUserSnsId={}", userSnsId);
            User user = userRepository.getUserByUserEmail(userEmail);

            if(user == null){
                UserLoginDto userLoginDto = new UserLoginDto();

                userLoginDto.setResult(false);
                userLoginDto.setErrorCode("500");
                userLoginDto.setUserEmail(userEmail);
                userLoginDto.setUserName(userName);
                userLoginDto.setUserSnsId(userSnsId);
                userLoginDto.setUserSnsKind(UserSnsKind.NAVER);

                return userLoginDto;
            }else if(user.getUserSnsKind() == null){
                UserLoginDto userLoginDto = new UserLoginDto();
                userLoginDto.setResult(false);
                userLoginDto.setErrorCode("409");
                userLoginDto.setUserEmail(userEmail);
                userLoginDto.setUserName(userName);
                userLoginDto.setUserSnsId(userSnsId);
                userLoginDto.setUserSnsKind(UserSnsKind.NAVER);

                return userLoginDto;
            }
            else{
                UserLoginDto userLoginDto = UserLoginDto
                        .builder()
                        .user(user)
                        .result(true)
                        .token(createToken(userEmail))
                        .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(userEmail))
                        .build();

                return userLoginDto;
            }
        }else{
            return null;
        }
    }

    public AuthorizationNaverDto naverLogin(
            String code,
            String callbackUrl,
            String state
    ) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String grantType = "authorization_code";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", grantType);
            params.add("client_id", NAVER_CLIENT_ID);
            params.add("client_secret", NAVER_KEY);
            params.add("redirect_uri", callbackUrl);
            params.add("code", code);
            params.add("state", state);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(NAVER_TOKEN_URL, request, String.class);
            return objectMapper.readValue(response.getBody(), AuthorizationNaverDto.class);
        } catch (JsonProcessingException e) {
            return null;
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

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(
                authentication.getName(),
                String.valueOf(authentication.getAuthorities()),
                claims,
                expiredDate
        );

        return jwtAuthToken.getToken(jwtAuthToken);
    }
}
