package moska.rebora.Common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseInfoResponse<T> extends BaseResponse{

    private T content;
}
