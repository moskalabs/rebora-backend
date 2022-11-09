package moska.rebora.Common;

import lombok.Data;

@Data
public class BaseInfoResponse<T> extends BaseResponse{

    private T content;
}
