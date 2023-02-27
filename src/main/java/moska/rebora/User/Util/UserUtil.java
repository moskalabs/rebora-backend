package moska.rebora.User.Util;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.UserSnsKind;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    public String convertSnsKind(UserSnsKind userSnsKind){
        switch (userSnsKind){
            case KAKAO:
                return "카카오";
            case NAVER:
                return "네이버";
            case APPLE:
                return "애플";
            default:
                return "";
        }
    }
}
