package moska.rebora.Recruitment.Repository;

import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecruitmentCustom {

    Page<UserRecruitmentListDto> getList(Pageable pageable, String userEmail, UserSearchCondition searchCondition);

    RecruitmentInfoDto getRecruitmentInfo(Long recruitmentId, String userEmail);
}
