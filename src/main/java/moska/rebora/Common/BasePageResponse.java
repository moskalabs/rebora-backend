package moska.rebora.Common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class BasePageResponse<T> {

    private Boolean result; //반환 성공 유무
    private Page<T> page;
}
