package moska.rebora.User.Controller;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.ResponseFormat;
import moska.rebora.Config.JwtAuthToken;
import moska.rebora.Config.JwtAuthTokenProvider;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Repository.UserRepository;
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

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {


    @Autowired
    UserServiceImpl userService;

    @PostMapping("/login")
    UserLoginDto login(@RequestParam("userEmail") String userEmail,
                       @RequestParam("password") String password) {
        log.info("user login userEmail={}, password={}", userEmail, password);

        return userService.login(userEmail, password);
    }

    @PostMapping("/signUp")
    UserLoginDto signUp(@RequestParam("userEmail") String userEmail,
                        @RequestParam("password") String password,
                        @RequestParam("userName") String userName,
                        @RequestParam("userNickname") String userNickname,
                        @RequestParam(value = "userPushYn", required = false) Boolean userPushYn,
                        @RequestParam(value = "userPushKey", required = false) String userPushKey) {
        return userService.signUp(userEmail, password, userName, userNickname, userPushYn, userPushKey);
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseFormat> check() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("name={}", authentication.getName());
        ResponseFormat res = new ResponseFormat().of("check");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/sendVerificationEmail")
    BaseResponse sendVerificationEmail(@RequestParam("userEmail") String userEmail,
                                       @RequestParam("verifyNumber") String verifyNumber) {
        return userService.sendVerificationEmail(userEmail, verifyNumber);
    }

    @GetMapping("/info")
    UserDto info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new UserDto(userService.getUserInfoByUserEmail(authentication.getName()));
    }

    @GetMapping("/checkRedundancyNickname")
    BaseResponse checkRedundancyNickname(@RequestParam("userNickname") String userNickname) {
        return userService.checkRedundancyNickname(userNickname);
    }

    @PostMapping("/sendPasswordChangeEmail")
    BaseResponse sendPasswordChangeEmail(@RequestParam(value = "userEmail") String userEmail,
                                         @RequestParam(value = "verifyNumber") String verifyNumber) {

        return userService.sendPasswordChangeEmail(userEmail, verifyNumber);
    }

    @PostMapping("/changePassword")
    UserLoginDto changePassword(@RequestParam("userEmail") String userEmail,
                                @RequestParam("password") String password) {
        return userService.changePassword(userEmail, password);
    }

}
