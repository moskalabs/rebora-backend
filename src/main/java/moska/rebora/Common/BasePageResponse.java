package moska.rebora.Common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@Schema(description = "페이징 DTO")
public class BasePageResponse<T> {

    @Schema(description = "성공 유뮤")
    private Boolean result; //반환 성공 유무
    private Page<T> page;
}
