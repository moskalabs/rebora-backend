package moska.rebora.Common;

import lombok.Getter;
import lombok.Setter;

/**
 * 기본 인증 정보
 */
@Getter
@Setter
public class BaseResponse {

    private Boolean result; //반환 성공 유무
    private String errorCode; //에러 코드
    private String message; // 메세지
}
