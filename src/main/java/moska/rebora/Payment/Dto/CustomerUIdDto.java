package moska.rebora.Payment.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Schema(description = "결제 Uid DTO")
@Data
public class CustomerUIdDto {

    @Schema(description = "유저 모집 아이디")
    Long userRecruitmentId;
    @Schema(description = "결제 번호 아이디")
    String customerUid;

    public CustomerUIdDto() {
    }
}
