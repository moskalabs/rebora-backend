package moska.rebora.User.Service;

import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserLoginDto;

public interface OathService {

    UserLoginDto login(String code, String callbackUrl, String state, UserSnsKind userSnsKind) throws Exception;
}
