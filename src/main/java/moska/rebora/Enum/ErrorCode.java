package moska.rebora.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND("404","COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR("500","COMMON-ERR-500","INTER SERVER ERROR"),
    DUPLICATION("409","MEMBER-ERR-400","EMAIL DUPLICATED"),
    JWT_UNAUTHORIZED("401","JWT-ERR-403","JWT EXPIRE"),
    BAD_REQUEST("400", "BAD_REQUEST", "BAD_REQUEST");

    private String status;
    private String errorCode;
    private String message;


}
