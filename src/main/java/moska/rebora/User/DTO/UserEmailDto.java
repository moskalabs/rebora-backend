package moska.rebora.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moska.rebora.Common.BaseResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "인증 이메일 DTO")
public class UserEmailDto extends BaseResponse {

    @Schema(description = "이메일 인증 키")
    private String authKey;
}
