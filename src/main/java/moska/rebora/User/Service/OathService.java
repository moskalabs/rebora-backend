package moska.rebora.User.Service;

import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserLoginDto;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;

public interface OathService {

    UserLoginDto login(String authToken, UserSnsKind snsKind);

    UserLoginDto appleLogin(
            @Param("userEmail") String userEmail,
            @Param("clientId") String clientId
    );

    HashMap<Object, Object> getUserInfo(UserSnsKind userSnsKind, String authToken);

    UserLoginDto syncUser(
            @Param("userEmail") String userEmail,
            @Param("userSnsKind") String userSnsKind,
            @Param("userSnsId") String userSnsId
    );
}
