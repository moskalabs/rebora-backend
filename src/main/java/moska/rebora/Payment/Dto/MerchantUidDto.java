package moska.rebora.Payment.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "주문번호 Uid Dto")
public class MerchantUidDto {

    @Schema(description = "유저 모집 아이디")
    Long userRecruitmentId;
    @Schema(description = "주문번호 Uid")
    String merchantUid;

    public MerchantUidDto() {
    }
}
