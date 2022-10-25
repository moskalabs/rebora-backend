package moska.rebora.Common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import moska.rebora.Enum.ErrorCode;

@Getter
@Setter
public class BaseResponse {
    private Boolean result;
    private String errorCode;
}
