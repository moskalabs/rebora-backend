package moska.rebora.User.Service;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Util;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationManager;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.Enum.UserGrade;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserEmailAuth;
import moska.rebora.User.Repository.UserEmailAuthRepository;
import moska.rebora.User.Repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordAuthAuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    @Autowired
    private UserEmailAuthRepository userEmailAuthRepository;

    @Autowired
    private UserEmailAuthService userEmailAuthService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Util util;


    private static final Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

    /**
     * 로그인
     *
     * @param userEmail 유저 이메일
     * @param password  비밀번호ㅓ
     * @return UserLoginDto
     */
    @Override
    public UserLoginDto login(@Param("userEmail") String userEmail, @Param("password") String password) {
        return UserLoginDto.builder().token(createToken(userEmail, password)).result(true).errorCode(null).build();
    }

    /**
     * 회원가입
     *
     * @param userEmail    유저 이메일
     * @param password     비밀번호
     * @param userName     유저이름
     * @param userNickname 유저 닉네임
     * @param userPushYn   유저 푸쉬 여부
     * @param userPushKey  유저 푸쉬 키
     * @return UserLoginDto
     */
    @Override
    public UserLoginDto signUp(@Param("userEmail") String userEmail,
                               @Param("password") String password,
                               @Param("userName") String userName,
                               @Param("userNickname") String userNickname,
                               @Param("userPushYn") Boolean userPushYn,
                               @Param("userPushKey") String userPushKey,
                               @Param("authKey") String authKey

    ) {
        UserEmailAuth userEmailAuth = userEmailAuthService.checkUserEmailAuth(userEmail, authKey, EmailAuthKind.SIGNUP);

        //비밀번호 인코딩
        String bcryptPassword = passwordEncoder.encode(password);

        //유저 객체 생성
        User user = User.builder()
                .userEmail(userEmail)
                .password(bcryptPassword)
                .userUseYn(true)
                .userPushYn(userPushYn)
                .userGrade(UserGrade.NORMAL)
                .userNickname(userNickname)
                .userName(userName)
                .userPushKey(userPushKey)
                .build();

        userRepository.save(user);

        userEmailAuth.expireAuth();
        userEmailAuthRepository.save(userEmailAuth);

        return UserLoginDto.builder().token(createToken(userEmail, password)).result(true).build();
    }

    public User getUserInfoByUserEmail(@Param("userEmail") @Valid String userEmail) {
        return userRepository.getUserByUserEmail(userEmail);
    }

    /**
     * 이메일 인증 발송
     *
     * @param userEmail     유저 이메일
     * @param emailAuthKind 이메일 종류
     * @return BaseResponse
     */
    @Override
    public BaseResponse sendVerificationEmail(@Param("userEmail") String userEmail, @Param("emailAuthKind") EmailAuthKind emailAuthKind) {

        String verifyNumber = util.createRandomString(6);
        BaseResponse baseResponse = new BaseResponse();
        try {
            if (userRepository.countUSerByUserEmail(userEmail) >= 1) {
                baseResponse.setResult(false);
                baseResponse.setErrorCode("409");
                baseResponse.setMessage("이미 존재하는 이메일입니다.");

            } else if (emailAuthKind == EmailAuthKind.SIGNUP) {
                userEmailAuthService.sendSignUpEmail(userEmail, verifyNumber);
                baseResponse.setResult(true);
            } else {
                userEmailAuthService.sendPasswordEmail(userEmail, verifyNumber);
                baseResponse.setResult(true);
            }
        } catch (NullPointerException e) {
            baseResponse.setResult(false);
            baseResponse.setErrorCode("500");
            baseResponse.setMessage(e.getMessage());
        }

        return baseResponse;
    }




    /**
     * 닉네임 중복확인
     *
     * @param userNickname 유저닉네임
     * @return BaseResponse
     */
    @Override
    public BaseResponse checkRedundancyNickname(@Param("userNickname") String userNickname) {
        BaseResponse baseResponse = new BaseResponse();
        int userCount = userRepository.countUserByUserNickname(userNickname);
        try {
            if (userCount == 0) {
                baseResponse.setResult(true);
            } else {
                throw new SQLIntegrityConstraintViolationException("이미 존재하는 유저 이름입니다.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            baseResponse.setResult(false);
            baseResponse.setErrorCode("409");
            baseResponse.setMessage(e.getMessage());
        }
        return baseResponse;
    }

    /**
     * 비밀번호 변경
     *
     * @param userEmail 유저이메일
     * @param password  비밀번호
     * @param authKey   인증키
     * @return UserLoginDto
     */
    @Override
    public UserLoginDto changePassword(String userEmail, String password, @Param("authKey") String authKey) {

        UserEmailAuth userEmailAuth = userEmailAuthService.checkUserEmailAuth(userEmail, authKey, EmailAuthKind.PASSWORD);

        User user = userRepository.getUserByUserEmail(userEmail);
        String bcryptPassword = passwordEncoder.encode(password);
        user.changePassword(bcryptPassword);
        userRepository.save(user);

        userEmailAuth.expireAuth();
        userEmailAuthRepository.save(userEmailAuth);

        String token = createToken(userEmail, password);
        return UserLoginDto.builder().token(token).result(true).build();
    }

    /**
     * 토큰 발행
     *
     * @param userEmail 유저이메일
     * @param password  비밀번호
     * @return String
     */
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
}
