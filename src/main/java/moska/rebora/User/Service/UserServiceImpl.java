package moska.rebora.User.Service;

import com.mchange.util.DuplicateElementException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Util;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationManager;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.Notification.Repository.NotificationRepository;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserEmailAuth;
import moska.rebora.User.Repository.UserEmailAuthRepository;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private PasswordAuthAuthenticationManager authenticationManager;

    private JwtAuthTokenProvider jwtAuthTokenProvider;

    private UserEmailAuthRepository userEmailAuthRepository;

    private UserEmailAuthService userEmailAuthService;

    PasswordEncoder passwordEncoder;

    NotificationRepository notificationRepository;

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
    public UserLoginDto login(@Param("userEmail") String userEmail,
                              @Param("password") String password,
                              @Param("userPushKey") String userPushKey) {
        User user = userRepository.getUserByUserEmail(userEmail);

        //없는 유저 일 경우
        if (user == null) {
            throw new NullPointerException("존재하지 않는 아이디 입니다. \n입력하신 내용을 다시 확인해 주세요.");
        }

        //탈퇴된 회원 일 경우
        if (!user.getUserUseYn()) {
            throw new NullPointerException("탈퇴된 회원입니다.");
        }

        //SNS 가입으로 된 회원 일 경우
        if (user.getUserSnsKind() != null) {
            throw new DuplicateElementException("SNS으로 회원가입한 회원입니다. 가입한 SNS : " + user.getUserSnsKind());
        }

        //푸쉬 키 변경
        user.changePushKey(userPushKey);
        userRepository.save(user);

        return UserLoginDto.builder()
                .token(createToken(userEmail, password))
                .result(true)
                .errorCode(null)
                .user(user)
                .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(userEmail))
                .build();
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
                               @Param("userPushYn") Boolean userPushNightYn,
                               @Param("userPushKey") String userPushKey,
                               @Param("authKey") String authKey,
                               @Param("userSnsKind") String userSnsKind,
                               @Param("userSnsId") String userSnsId,
                               @Param("userBirth") String userBirth,
                               @Param("isAuthenticated") Boolean isAuthenticated

    ) throws SQLIntegrityConstraintViolationException {

        UserEmailAuth userEmailAuth = null;
        try {

            //인증 받아오기
            userEmailAuth = userEmailAuthService.checkUserEmailAuth(userEmail, authKey, EmailAuthKind.SIGNUP);

            //비밀번호 인코딩
            String bcryptPassword = passwordEncoder.encode(password);
            UserSnsKind snsKind = null;

            if (userSnsKind != null) {
                snsKind = UserSnsKind.valueOf(userSnsKind);
            }

            //유저 객체 생성
            User user = User.builder()
                    .userEmail(userEmail)
                    .password(bcryptPassword)
                    .userUseYn(true)
                    .userPushYn(userPushYn)
                    .userGrade(UserGrade.NORMAL)
                    .userNickname(userNickname)
                    .userPushNightYn(userPushNightYn)
                    .userName(userName)
                    .userPushKey(userPushKey)
                    .userSnsKind(snsKind)
                    .userSnsId(userSnsId)
                    .userBirth(userBirth)
                    .isAuthenticated(isAuthenticated)
                    .userAge(0)
                    .build();

            userRepository.save(user);

            userEmailAuth.expireAuth();

            userEmailAuthRepository.save(userEmailAuth);

            return UserLoginDto.builder()
                    .token(createToken(userEmail, password))
                    .result(true)
                    .user(user)
                    .notificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(userEmail))
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new SQLIntegrityConstraintViolationException("중복된 이메일 또는 닉네임입니다.");
        }
    }

    /**
     * 유저 이메일로 정보 가져오기
     *
     * @param userEmail 유저 이메일
     * @return UserDto
     */
    public UserDto getUserInfoByUserEmail(@Param("userEmail") @Valid String userEmail) {

        UserDto userDto = new UserDto(userRepository.getUserByUserEmail(userEmail));

        //유저 알림 카운트 가져오기
        userDto.setNotificationCount(notificationRepository.countNotificationByNotificationReadYnFalseAndUserUserEmail(userEmail));
        return userDto;
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

        //인증 번호 생성
        String verifyNumber = util.createRandomString(6);
        BaseResponse baseResponse = new BaseResponse();
        try {

            //비밀번호 찾기 일 경우
            if (emailAuthKind == EmailAuthKind.PASSWORD) {
                userEmailAuthService.sendPasswordEmail(userEmail, verifyNumber);
                baseResponse.setResult(true);

                //회원 가입 일 경우
            } else {
                //이미 가입된 아이디 일 경우
                if (userRepository.countUSerByUserEmail(userEmail) >= 1) {
                    throw new DuplicateElementException("이미 가입된 아이디 입니다. \n로그인을 진행해주세요.");
                } else {
                    userEmailAuthService.sendSignUpEmail(userEmail, verifyNumber);
                    baseResponse.setResult(true);
                }
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

            //중복된 닉네임이 있을 없을 경우
            if (userCount == 0) {
                baseResponse.setResult(true);
                //중복된 닉네임이 있을 있을 경우
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
     * @return BaseResponse
     */
    @Override
    public BaseResponse changePassword(String userEmail, String password, @Param("authKey") String authKey) {

        //인증 검증
        UserEmailAuth userEmailAuth = userEmailAuthService.checkUserEmailAuth(userEmail, authKey, EmailAuthKind.PASSWORD);

        User user = userRepository.getUserByUserEmail(userEmail);
        String bcryptPassword = passwordEncoder.encode(password);
        user.changePassword(bcryptPassword);
        userRepository.save(user);

        userEmailAuth.expireAuth();
        userEmailAuthRepository.save(userEmailAuth);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        return baseResponse;
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

    /**
     * 유저 유효성 검사
     *
     * @param user 유저 엔티티
     * @return boolean
     */
    @Override
    public boolean isOnValidUser(User user) {

        //유저 사용 여부
        if (!user.getUserUseYn()) {
            return false;

            //탈퇴 및 휴면 계정 여부
        } else if (user.getUserGrade().equals(UserGrade.WITHDRAWAL) || user.getUserGrade().equals(UserGrade.DORMANCY)) {
            return false;
        } else {
            return true;
        }
    }
}
