package moska.rebora.User.Controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Service.OathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/user/oath")
@Slf4j
public class OathController {

    @Autowired
    OathService oathService;

    @PostMapping("/naverLogin")
    public UserLoginDto naverLogin(
            @RequestParam String authToken
    ) {
        return oathService.login(authToken, UserSnsKind.NAVER);
    }

    @PostMapping("/kakaoLogin")
    public UserLoginDto kakaoLogin(
            @RequestParam String authToken
    ) {
        return oathService.login(authToken, UserSnsKind.KAKAO);
    }

    @PostMapping("/appleLogin")
    public UserLoginDto appleLogin(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String userEmail
    ) {
        return oathService.appleLogin(userEmail, clientId);
    }

    @GetMapping("/getSnsInfo")
    public BaseInfoResponse<SnsInfo> getSnsInfo(String userSnsKind, String authToken) {

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

    @PostMapping("/syncUser")
    public UserLoginDto syncUser(String userEmail, String userSnsKind, String userSnsId) {
        return oathService.syncUser(userEmail, userSnsKind, userSnsId);
    }
}

@Data
class SnsInfo {
    private UserSnsKind userSnsKind;
    private String userSnsId;
}
