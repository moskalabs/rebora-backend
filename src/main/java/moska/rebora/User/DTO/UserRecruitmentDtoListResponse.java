package moska.rebora.User.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import moska.rebora.Common.BaseResponse;
import org.springframework.data.domain.Page;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRecruitmentDtoListResponse extends BaseResponse {

    Page<UserRecruitmentListDto> userRecruitmentList;
}
