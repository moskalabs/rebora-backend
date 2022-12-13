package moska.rebora.Recruitment.Repository;

import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentBatch {

    List<Recruitment> getBatchRecruitmentList(
            @Param("recruitmentStatus") RecruitmentStatus recruitmentStatus,
            @Param("condition") UserSearchCondition condition);

    List<Recruitment> getBatchFinishMovie();
}
