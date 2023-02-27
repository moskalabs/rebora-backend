package moska.rebora.User.Controller;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.Main.Dto.MainDto;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserEmailDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import moska.rebora.User.Service.UserEmailAuthService;
import moska.rebora.User.Service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "유저", description = "유저 정보")
@AllArgsConstructor
public class UserController {
    UserService userService;
    UserEmailAuthService userEmailAuthService;
    UserRepository userRepository;

    /**
     * 로그인
     *
     * @param userEmail 유저 이메일
     * @param password  패스워드
     * @return UserLoginDto
     */
    @Tag(name = "유저")
    @Operation(summary = "로그인")
    @PostMapping("/login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", value = "유저 이메일", required = true),
            @ApiImplicitParam(name = "password", value = "패스워드", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인 정보 가져오기 성공", content = @Content(schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = "500", description = "로그인 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    UserLoginDto login(@RequestParam("userEmail") String userEmail,
                       @RequestParam("password") String password,
                       @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        log.info("user login userEmail={}, password={}", userEmail, password);
        return userService.login(userEmail, password, userPushKey);
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
    @Tag(name = "유저")
    @Operation(summary = "회원가입", description = "인증 후 30분 이내에만 회원가입 가능합니다.")
    @PostMapping("/signUp")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", value = "유저 이메일", required = true),
            @ApiImplicitParam(name = "password", value = "패스워드", required = true),
            @ApiImplicitParam(name = "userName", value = "유저 이름", required = true),
            @ApiImplicitParam(name = "userNickname", value = "유저 닉네임", required = true),
            @ApiImplicitParam(name = "userPushYn", value = "유저 푸쉬 여부", required = false),
            @ApiImplicitParam(name = "userPushNightYn", value = "유저 푸쉬 야간 여부", required = false),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false),
            @ApiImplicitParam(name = "authKey", value = "이메일 인증 키", required = true),
    })
    UserLoginDto signUp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("password") String password,
            @RequestParam("userName") String userName,
            @RequestParam("userNickname") String userNickname,
            @RequestParam(value = "userPushYn", required = false) Boolean userPushYn,
            @RequestParam(value = "userPushNightYn", required = false) Boolean userPushNightYn,
            @RequestParam(value = "userPushKey", required = false) String userPushKey,
            @RequestParam(value = "authKey") String authKey,
            @RequestParam(value = "userSnsKind", required = false) String userSnsKind,
            @RequestParam(value = "userSnsId", required = false) String userSnsId,
            @RequestParam(required = false, defaultValue = "1990-01-01") String userBirth,
            @RequestParam(required = false, defaultValue = "false") Boolean isAuthenticated
    ) throws SQLIntegrityConstraintViolationException {

        return userService.signUp(
                userEmail,
                password,
                userName,
                userNickname,
                userPushYn,
                userPushNightYn,
                userPushKey,
                authKey,
                userSnsKind,
                userSnsId,
                userBirth,
                isAuthenticated
        );
    }

    /**
     * 유저 인증메일 전송
     *
     * @param userEmail     유저 이메일
     * @param emailAuthKind 유저 이메일 인증 종류
     * @return BaseResponse
     */
    @Tag(name = "유저")
    @Operation(summary = "인증이메일 전송", description = "해당 이메일에 6자리 랜덤 문자를 보냅니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", value = "유저 이메일", required = true),
            @ApiImplicitParam(name = "emailAuthKind", value = "이메일 권한 종류", required = true),
    })
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
    @Tag(name = "유저", description = "정보 가져오기")
    @Operation(summary = "유저 정보")
    @GetMapping("/info")
    UserDto info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserInfoByUserEmail(authentication.getName());
    }

    /**
     * 유저 닉네임 체크
     *
     * @param userNickname 유저 닉네임
     * @return BaseResponse
     */
    @Tag(name = "유저")
    @Operation(summary = "닉네임 체크")
    @ApiImplicitParam(name = "userNickname", value = "유저 닉네임", required = true)
    @GetMapping("/checkRedundancyNickname")
    BaseResponse checkRedundancyNickname(@RequestParam("userNickname") String userNickname) {
        return userService.checkRedundancyNickname(userNickname);
    }

    /**
     * 패스워드 변경
     *
     * @param userEmail 유저 이메일
     * @param password  패스워드
     * @param authKey   인증 키
     * @return UserLoginDto
     */
    @Tag(name = "유저")
    @Operation(summary = "비밀번호 변경", description = "30분 내로 인증키로 인증해야 가능합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", value = "유저 이메일", required = true),
            @ApiImplicitParam(name = "password", value = "비밀번호", required = true),
            @ApiImplicitParam(name = "authKey", value = "인증키", required = true)
    })
    @PostMapping("/changePassword")
    BaseResponse changePassword(@RequestParam("userEmail") String userEmail,
                                @RequestParam("password") String password,
                                @RequestParam("authKey") String authKey
    ) {
        return userService.changePassword(userEmail, password, authKey);
    }

    /**
     * 인증 이메일 검사
     *
     * @param userEmail     유저 이메일
     * @param verifyNumber  인증번호
     * @param emailAuthKind 유저 인증 종류
     * @return UserEmailDto
     */
    @Tag(name = "유저")
    @Operation(summary = "인증이메일 검사")
    @PostMapping("/validationEmailCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", value = "유저 이메일", required = true),
            @ApiImplicitParam(name = "verifyNumber", value = "인증 번호", required = true),
            @ApiImplicitParam(name = "emailAuthKind", value = "인증 종류", required = true)
    })
    UserEmailDto validationEmailCode(@RequestParam("userEmail") String userEmail,
                                     @RequestParam("verifyNumber") String verifyNumber,
                                     @RequestParam("emailAuthKind") EmailAuthKind emailAuthKind) {
        return userEmailAuthService.validationEmailCode(userEmail, verifyNumber, emailAuthKind);
    }

    /**
     * 탈퇴하기
     *
     * @param userId 유저 아이디
     * @return BaseResponse
     */
    @Tag(name = "유저", description = "회원 탈퇴")
    @Operation(summary = "회원 탈퇴")
    @ApiImplicitParam(name = "userId", value = "유저 아이디", required = true)
    @PutMapping("/withdrawal/{userId}")
    BaseResponse withdrawal(@PathVariable Long userId) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        User user = userRepository.getUserById(userId);

        if (!user.getUserEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new JwtException("옳바르지 않은 인증입니다.");
        }

        user.withdrawalUser();
        userRepository.save(user);

        return baseResponse;
    }
}
