package moska.rebora.User.Service;

import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.ApplePublicKeyDto;
import moska.rebora.User.DTO.UserLoginDto;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

public interface OathService {

    UserLoginDto login(
            String authToken,
            UserSnsKind snsKind,
            @Param("userPushKey") String userPushKey
    );

    HashMap<Object, Object> getUserInfo(UserSnsKind userSnsKind, String authToken);

    UserLoginDto signUpSns(
            String userEmail,
            String userName,
            String userNickname,
            UserSnsKind userSnsKind,
            String userSnsId,
            Boolean userPushYn,
            Boolean userPushNightYn,
            String userPushKey,
            String userBirth,
            Boolean isAuthenticated
    );

    List<ApplePublicKeyDto> getPublicAppleKeys();
}
