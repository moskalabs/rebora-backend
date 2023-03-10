package moska.rebora.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.UserCarrierType;
import org.json.simple.JSONObject;

@Data
@Slf4j
@Schema(description = "유저 본인 인증 Dto")
public class UserAuthenticatedDto {
    @Schema(description = "유저 생년월일")
    private String userBirth;
    @Schema(description = "유저 이름")
    private String userName;
    @Schema(description = "유저 통신사 타입")
    private UserCarrierType userCarrierType;
    @Schema(description = "유저 핸드폰 번호")
    private String userPhone;

    public UserAuthenticatedDto() {
    }

    public UserAuthenticatedDto(JSONObject jsonObject) {
        this.userBirth = (String) jsonObject.get("birthday");
        this.userCarrierType = UserCarrierType.valueOf((String) jsonObject.get("carrier"));
        this.userName = (String) jsonObject.get("name");
        this.userPhone = (String) jsonObject.get("phone");
    }
}
