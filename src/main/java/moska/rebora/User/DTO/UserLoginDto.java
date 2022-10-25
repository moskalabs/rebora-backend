package moska.rebora.User.DTO;

import lombok.Builder;
import lombok.Getter;
import moska.rebora.Common.BaseResponse;

@Getter
public class UserLoginDto extends BaseResponse {

    private String token;

    @Builder
    public UserLoginDto(String token, Boolean result, String errorCode) {
        setResult(result);
        setErrorCode(errorCode);
        this.token = token;
    }
}
