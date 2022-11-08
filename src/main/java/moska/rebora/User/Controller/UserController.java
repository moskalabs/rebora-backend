package moska.rebora.User.Controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.ResponseFormat;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Repository.UserRepository;
import moska.rebora.User.Service.UserEmailAuthService;
import moska.rebora.User.Service.UserService;
import moska.rebora.User.Service.UserServiceImpl;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.cert.CertificateExpiredException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    UserEmailAuthService userEmailAuthService;

    /**
     * 로그인
     *
     * @param userEmail 유저 이메일
     * @param password  패스워드
     * @return UserLoginDto
     */
    @PostMapping("/login")
    UserLoginDto login(@RequestParam("userEmail") String userEmail,
                       @RequestParam("password") String password) {
        log.info("user login userEmail={}, password={}", userEmail, password);

        return userService.login(userEmail, password);
    }

    /**
     * 회원 가입
     *
     * @param userEmail    유저 이메일
     * @param password     패스워드
     * @param userName     유저 이름
     * @param userNickname 유저 닉네임
     * @param userPushYn   유저 푸쉬 여부
     * @param userPushKey  유저 푸쉬 키
     * @param authKey      유저 인증 키
     * @return UserLoginDto
     */
    @PostMapping("/signUp")
    UserLoginDto signUp(@RequestParam("userEmail") String userEmail,
                        @RequestParam("password") String password,
                        @RequestParam("userName") String userName,
                        @RequestParam("userNickname") String userNickname,
                        @RequestParam(value = "userPushYn", required = false) Boolean userPushYn,
                        @RequestParam(value = "userPushKey", required = false) String userPushKey,
                        @RequestParam(value = "authKey") String authKey
    ) throws SQLIntegrityConstraintViolationException {
        return userService.signUp(userEmail, password, userName, userNickname, userPushYn, userPushKey, authKey) ;
    }

    /**
     * 유저 인증메일 전송
     *
     * @param userEmail 유저 이메일
     * @param emailAuthKind 유저 이메일 인증 종류
     * @return BaseResponse
     */
    @PostMapping("/sendVerificationEmail")
    BaseResponse sendVerificationEmail(@RequestParam("userEmail") String userEmail,
                                       @RequestParam("emailAuthKind") EmailAuthKind emailAuthKind) {
        return userService.sendVerificationEmail(userEmail, emailAuthKind);
    }

    /**
     * 유저 정보 가져오기
     *
     * @return UserDto
     */
    @GetMapping("/info")
    UserDto info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new UserDto(userService.getUserInfoByUserEmail(authentication.getName()));
    }

    /**
     * 유저 닉네임 체크
     *
     * @param userNickname 유저 닉네임
     * @return BaseResponse
     */
    @GetMapping("/checkRedundancyNickname")
    BaseResponse checkRedundancyNickname(@RequestParam("userNickname") String userNickname) {
        return userService.checkRedundancyNickname(userNickname);
    }

    /**
     * 패스워드 변경
     *
     * @param userEmail 유저 이메일
     * @param password 패스워드
     * @param authKey 인증 키
     * @return UserLoginDto
     */
    @PostMapping("/changePassword")
    UserLoginDto changePassword(@RequestParam("userEmail") String userEmail,
                                @RequestParam("password") String password,
                                @RequestParam("authKey") String authKey
    ) {
        return userService.changePassword(userEmail, password, authKey);
    }

    /**
     * 인증 이메일 검사
     *
     * @param userEmail 유저 이메일
     * @param verifyNumber 인증번호
     * @param emailAuthKind 유저 인증 종류
     * @return JSONObject
     */
    @PostMapping("/validationEmailCode")
    JSONObject validationEmailCode(@RequestParam("userEmail") String userEmail,
                                   @RequestParam("verifyNumber") String verifyNumber,
                                   @Param("emailAuthKind") EmailAuthKind emailAuthKind) {
        return userEmailAuthService.validationEmailCode(userEmail, verifyNumber, emailAuthKind);
    }
}
