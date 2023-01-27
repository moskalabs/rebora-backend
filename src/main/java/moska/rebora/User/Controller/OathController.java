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

    @PostMapping("/kakaoLogin")
    public UserLoginDto kakaoLogin(
            @RequestParam String authToken,
            @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        return oathService.login(authToken, UserSnsKind.KAKAO, userPushKey);
    }

    @PostMapping("/appleLogin")
    public UserLoginDto appleLogin(
            @RequestParam String idToken,
            @RequestParam(value = "userPushKey", required = false, defaultValue = "") String userPushKey
    ) {
        return oathService.login(idToken, UserSnsKind.APPLE, userPushKey);
    }

    @CrossOrigin(origins = "https://appleid.apple.com")
    @PostMapping(value = "/appleCallback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Object> appleCallback(EchoAppleLogin body) throws URISyntaxException {

        String androidPackage = "com.moca.robora";
        String androidScheme = "signinwithapple";
        String callback = String.format("intent://callback?code=%s&id_token=%s#Intent;package=%s;scheme=%s;end", body.getCode(), body.getId_token(), androidPackage, androidScheme);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(callback));
        return new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);

    }

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
