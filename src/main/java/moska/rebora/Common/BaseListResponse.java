package moska.rebora.Common;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseListResponse<T> extends BaseResponse{

    private List<T> list;
}
