package moska.rebora.User.Service;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static Date expiredDate = Date.from(LocalDateTime.now().plusYears(1L).atZone(ZoneId.systemDefault()).toInstant());

    @Override
    public UserLoginDto login(@Param("userEmail") String userEmail, @Param("password") String password) {
        UserLoginDto userLoginDto = UserLoginDto.builder().token(createToken(userEmail, password)).result(true).errorCode(null).build();
        return userLoginDto;
    }

    @Override
    public UserLoginDto signUp(@Param("userEmail") String userEmail,
                               @Param("password") String password,
                               @Param("userName") String userName,
                               @Param("userNickname") String userNickname,
                               @Param("userPushYn") Boolean userPushYn,
                               @Param("userPushKey") String userPushKey
    ) {

        String bcryptPassword = passwordEncoder.encode(password);

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

    public String createToken(String userEmail, String password) {

        PasswordAuthAuthenticationToken token = new PasswordAuthAuthenticationToken(userEmail, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, String> claims = new HashMap<>();
        claims.put("id", token.getId().toString());
        claims.put("userName", token.getUserName());
        claims.put("role", "role");
        claims.put("userEmail", token.getUserEmail());

        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.createAuthToken(
                "test@test.com",
                "role",
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
                "test@test.com",
                "role",
                claims,
                expiredDate
        );

        return jwtAuthToken.getToken(jwtAuthToken);
    }
}
