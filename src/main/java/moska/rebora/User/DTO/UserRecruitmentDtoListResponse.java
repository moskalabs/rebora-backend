package moska.rebora.User.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import moska.rebora.Common.BaseResponse;
import org.springframework.data.domain.Page;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRecruitmentDtoListResponse extends BaseResponse {

    Page<UserRecruitmentListDto> userRecruitmentList;
}
