package moska.rebora.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moska.rebora.Common.BaseResponse;
import org.springframework.data.domain.Page;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "유저_모집 페이징 DTO")
public class UserRecruitmentDtoListResponse extends BaseResponse {

    @Schema(description = "유저_모집 페이지 리스트")
    Page<UserRecruitmentListDto> userRecruitmentList;
}
