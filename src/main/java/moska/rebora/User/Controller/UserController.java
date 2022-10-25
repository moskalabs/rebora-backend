package moska.rebora.User.Controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.ResponseFormat;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Repository.UserRepository;
import moska.rebora.User.Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {


    @Autowired
    UserServiceImpl userService;

    @PostMapping("/login")
    ResponseEntity<UserLoginDto> login(@Param("userEmail") String userEmail, @Param("password") String password) {
        log.info("user login userEmail={}, password={}", userEmail, password);

        return new ResponseEntity<>(userService.login(userEmail, password), HttpStatus.OK);
    }

    @PostMapping("/signUp")
    ResponseEntity<UserLoginDto> signUp(@Param("userEmail") String userEmail,
                                        @Param("password") String password,
                                        @Param("userName") String userName,
                                        @Param("userNickname") String userNickname,
                                        @Param("userPushYn") Boolean userPushYn,
                                        @Param("userPushKey") String userPushKey) {
        return new ResponseEntity<>(userService.signUp(userEmail, password, userName, userNickname, userPushYn, userPushKey), HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseFormat> check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("name={}", authentication.getName());
        ResponseFormat res = new ResponseFormat().of("check");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
