package moska.rebora.User.Controller;

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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/user/oath")
@Slf4j
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
    @PostMapping("/naverLogin")
    public UserLoginDto naverLogin(
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
    @PostMapping("/appleLogin")
    public UserLoginDto appleLogin(
            @RequestParam String idToken,
            @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        return oathService.login(idToken, UserSnsKind.APPLE, userPushKey);
    }

    /**
     * 애플 안드로이드 콜백
     *
     * @param jsonObject 파라미터 받기
     * @return ResponseEntity<Object>
     * @throws URISyntaxException URL 통신 오류
     */
    @CrossOrigin(origins = "https://appleid.apple.com")
    @PostMapping(value = "/appleCallback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> appleCallback(JSONObject jsonObject) throws URISyntaxException {

        String androidPackage = "com.moca.robora";
        String androidScheme = "signinwithapple";
        JSONObject user = (JSONObject) jsonObject.get("user");
        JSONObject name = (JSONObject) user.get("name");
        String callback = String.format("intent://callback?code=%s&id_token=%s&firstName=%s&lastName=%s#Intent;package=%s;scheme=%s;end", jsonObject.get("code"), jsonObject.get("id_token"), name.get("firstName"), name.get("lastName"), androidPackage, androidScheme);
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
    public UserLoginDto signUpSns(
            @RequestParam String userEmail,
            @RequestParam String userName,
            @RequestParam String userNickname,
            @RequestParam String userSnsKind,
            @RequestParam String userSnsId,
            @RequestParam(required = false, defaultValue = "false") Boolean userPushYn,
            @RequestParam(required = false, defaultValue = "false") Boolean userPushNightYn,
            @RequestParam(required = false, defaultValue = "") String userPushKey
    ) {
        return oathService.signUpSns(userEmail, userName, userNickname, UserSnsKind.valueOf(userSnsKind), userSnsId, userPushYn, userPushNightYn, userPushKey);
    }

    /**
     * SNS 정보 가져오기
     *
     * @param userSnsKind 유저 SNS 종류
     * @param authToken   토큰
     * @return BaseInfoResponse<SnsInfo>
     */
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
