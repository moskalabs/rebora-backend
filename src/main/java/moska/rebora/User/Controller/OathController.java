package moska.rebora.User.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.shaded.json.parser.JSONParser;
import com.nimbusds.jose.shaded.json.parser.ParseException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.EchoAppleLogin;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Service.OathService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user/oath")
@Slf4j
@Tag(name = "SNS 유저", description = "SNS 유저")
@AllArgsConstructor
public class OathController {

    OathService oathService;

    /**
     * 네이버 로그인
     *
     * @param authToken   토큰
     * @param userPushKey 유저 푸쉬 키
     * @return UserLoginDto
     */
    @Tag(name = "SNS 유저")
    @Operation(summary = "네이버 로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "토큰", required = true),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "네이버 로그인 정보 가져오기 성공 result가 false일 경우 회원가입", content = @Content(schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = "500", description = "로그인 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/naverLogin")
    UserLoginDto naverLogin(
            @RequestParam String authToken,
            @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto = oathService.login(authToken, UserSnsKind.NAVER, userPushKey);
        log.info("message={}", userLoginDto.getMessage());
        log.info("userSnsId={}", userLoginDto.getUserSnsId());
        return userLoginDto;
    }

    /**
     * 카카오 로그인
     *
     * @param authToken   토큰
     * @param userPushKey 유저 푸쉬 키
     * @return UserLoginDto
     */
    @Tag(name = "SNS 유저")
    @Operation(summary = "카카오 로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authToken", value = "토큰", required = true),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 정보 가져오기 성공 result가 false일 경우 회원가입", content = @Content(schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = "500", description = "로그인 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/kakaoLogin")
    public UserLoginDto kakaoLogin(
            @RequestParam String authToken,
            @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        return oathService.login(authToken, UserSnsKind.KAKAO, userPushKey);
    }

    /**
     * 애플 로그인
     *
     * @param idToken     아이디 토큰
     * @param userPushKey 유저 푸쉬 키
     * @return UserLoginDto
     */
    @Tag(name = "SNS 유저")
    @Operation(summary = "애플  로그인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "idToken", value = "토큰", required = true),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "애플 로그인 정보 가져오기 성공 result가 false일 경우 회원가입", content = @Content(schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = "500", description = "로그인 실패", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/appleLogin")
    public UserLoginDto appleLogin(
            @RequestParam String idToken,
            @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        return oathService.login(idToken, UserSnsKind.APPLE, userPushKey);
    }

    @ApiIgnore
    @CrossOrigin(origins = "https://appleid.apple.com")
    @PostMapping(value = "/appleCallback")
    public ResponseEntity<Object> appleCallback(String code, String id_token, String user) throws ParseException, URISyntaxException {

        String firstName = "";
        String lastName = "";

        log.info("code={}", code);
        log.info("id_token={}", id_token);
        log.info("user={}", user);

        if (user != null) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(user);
            JsonObject name = element.getAsJsonObject().get("name").getAsJsonObject();
            String email = element.getAsJsonObject().get("email").toString();
            firstName = name.get("firstName").toString().replace("\"", "");
            lastName = name.get("lastName").toString().replace("\"", "");

            log.info("email={}", email);
            log.info("firstName={}", firstName);
            log.info("lastName={}", lastName);
        }

        String androidPackage = "com.moca.rebora";
        String androidScheme = "signinwithapple";

        String callback = String.format("intent://callback?code=%s&id_token=%s&firstName=%s&lastName=%s#Intent;package=%s;scheme=%s;end", code, id_token, firstName, lastName, androidPackage, androidScheme);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(callback));
        return new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
    }

    /**
     * SNS 회원가입
     *
     * @param userEmail       유저 이메일
     * @param userName        유저 이름
     * @param userNickname    유저 닉네임
     * @param userSnsKind     유저 SNS 종류
     * @param userSnsId       유저 SNS Id
     * @param userPushYn      유저 푸쉬 여부
     * @param userPushNightYn 유저 푸쉬 야간 여부
     * @param userPushKey     유저 푸쉬 키
     * @return UserLoginDto
     */

    @PostMapping("/signUpSns")
    @Tag(name = "SNS 유저")
    @Operation(summary = "SNS 회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userEmail", value = "유저 이메일", required = true),
            @ApiImplicitParam(name = "userName", value = "유저 이름", required = true),
            @ApiImplicitParam(name = "userNickname", value = "유저 닉네임", required = true),
            @ApiImplicitParam(name = "userSnsKind", value = "유저 SNS 종류", required = true),
            @ApiImplicitParam(name = "userSnsId", value = "유저 SNS ID", required = true),
            @ApiImplicitParam(name = "userPushYn", value = "유저 푸쉬 여부", required = false),
            @ApiImplicitParam(name = "userPushNightYn", value = "유저 야간 푸쉬 여부", required = false),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false),
            @ApiImplicitParam(name = "userBirth", value = "유저 생년월일", required = false),
            @ApiImplicitParam(name = "isAuthenticated", value = "유저 본인인증 여부", required = false)
    })
    public UserLoginDto signUpSns(
            @RequestParam String userEmail,
            @RequestParam String userName,
            @RequestParam String userNickname,
            @RequestParam String userSnsKind,
            @RequestParam String userSnsId,
            @RequestParam(required = false, defaultValue = "false") Boolean userPushYn,
            @RequestParam(required = false, defaultValue = "false") Boolean userPushNightYn,
            @RequestParam(required = false, defaultValue = "") String userPushKey,
            @RequestParam(required = false, defaultValue = "1990-01-01") String userBirth,
            @RequestParam(required = false, defaultValue = "false") Boolean isAuthenticated
    ) {
        return oathService.signUpSns(userEmail, userName, userNickname, UserSnsKind.valueOf(userSnsKind), userSnsId, userPushYn, userPushNightYn, userPushKey, userBirth, isAuthenticated);
    }

    /**
     * SNS 정보 가져오기
     *
     * @param userSnsKind 유저 SNS 종류
     * @param authToken   토큰
     * @return BaseInfoResponse<SnsInfo>
     */
    @ApiIgnore
    @GetMapping("/getSnsInfo")
    public BaseInfoResponse<SnsInfo> getSnsInfo(
            String userSnsKind,
            String authToken
    ) {

        BaseInfoResponse<SnsInfo> baseInfoResponse = new BaseInfoResponse<>();
        SnsInfo snsInfo = new SnsInfo();

        HashMap<Object, Object> prmMap = new HashMap<Object, Object>();
        prmMap = oathService.getUserInfo(UserSnsKind.valueOf(userSnsKind), authToken);
        snsInfo.setUserSnsKind(UserSnsKind.valueOf(userSnsKind));
        snsInfo.setUserSnsId(String.valueOf(prmMap.get("snsId")));
        baseInfoResponse.setResult(true);
        baseInfoResponse.setContent(snsInfo);

        return baseInfoResponse;
    }
}

@Data
class SnsInfo {
    private UserSnsKind userSnsKind;
    private String userSnsId;
}

@Data
class AppleCallbackDto {
    private String email;
    private HashMap<String, Object> fullName;
    private String identityToken;
    private String code;

}
