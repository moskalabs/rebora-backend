package moska.rebora.User.Service;

import moska.rebora.User.DTO.UserLoginDto;

public interface OathService {

    public UserLoginDto naverLogin(String code, String callbackUrl, String state);
}
