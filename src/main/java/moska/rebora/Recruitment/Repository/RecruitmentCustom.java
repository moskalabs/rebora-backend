package moska.rebora.Recruitment.Repository;

import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RecruitmentCustom {

    Page<UserRecruitmentListDto> getList(Pageable pageable, String userEmail, String userBirth, UserSearchCondition searchCondition);

    RecruitmentInfoDto getRecruitmentInfo(Long recruitmentId, String userEmail);

    Optional<Recruitment> getOptionalRecruitmentById(Long recruitmentId);
}
