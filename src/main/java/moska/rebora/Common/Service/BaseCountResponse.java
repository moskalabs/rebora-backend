package moska.rebora.Common.Service;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class BaseCountResponse {

    @Schema(description = "성공 유뮤")
    private Boolean result; //반환 성공 유무
    private Integer count;
}
