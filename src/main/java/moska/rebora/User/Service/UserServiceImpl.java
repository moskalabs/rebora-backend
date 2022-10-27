package moska.rebora.User.Service;

import io.jsonwebtoken.JwtException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Util;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationManager;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.UserGrade;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    PasswordEncoder passwordEncoder;

    @Autowired
    Util util;


    private static Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

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
                               @Param("userPushKey") String userPushKey
    ) {

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

        return UserLoginDto.builder().token(createToken(userEmail, password)).result(true).build();
    }

    public User getUserInfoByUserEmail(@Param("userEmail") @Valid String userEmail){
        return userRepository.getUserByUserEmail(userEmail);
    }


    public BaseResponse sendVerificationEmail(@Param("userEmail") String userEmail,
                                              @Param("verifyNumber") String verifyNumber) {

        BaseResponse baseResponse = new BaseResponse();
        int userCount = userRepository.countUSerByUserEmail(userEmail);

        if (userCount >= 1) {
            baseResponse.setResult(false);
            baseResponse.setErrorCode("409");
        } else {
            String subject = "리보라 인증 메일입니다.";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("리보라 인증 번호입니다. \n");
            stringBuilder.append(verifyNumber);
            util.sendEmail(userEmail, subject, stringBuilder.toString());
            baseResponse.setResult(true);
        }

        return baseResponse;
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

    public String createToken(PasswordAuthAuthenticationToken token) {
        Map<String, String> claims = new HashMap<>();
        claims.put("id", token.getId().toString());
        claims.put("userName", token.getUserName());
        claims.put("role", "role");
        claims.put("userEmail", token.getUserEmail());

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(
                token.getUserEmail(),
                "role",
                claims,
                expiredDate
        );

        return jwtAuthToken.getToken(jwtAuthToken);
    }
}
