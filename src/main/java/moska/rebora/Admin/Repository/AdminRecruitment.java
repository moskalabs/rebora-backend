package moska.rebora.Admin.Repository;

import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface AdminRecruitment {

    Page<RecruitmentInfoDto> getRecruitmentPage(Pageable pageable, UserSearchCondition userSearchCondition);

    RecruitmentInfoDto getAdminRecruitmentInfo(@Param("recruitmentId") Long recruitmentId);
}
