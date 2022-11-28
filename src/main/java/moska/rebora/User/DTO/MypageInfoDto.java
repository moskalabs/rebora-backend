package moska.rebora.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moska.rebora.Common.BaseResponse;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "마이페이지 정보 DTO")
public
class MypageInfoDto extends BaseResponse {
    @Schema(description = "참여 내역 횟수")
    private Long countParticipationHistory;
    @Schema(description = "내가 모집한 모집 개수")
    private Long countMyRecruiter;
}