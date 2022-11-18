package moska.rebora.Recruitment.Repository;

import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;

import java.util.List;

public interface RecruitmentBatch {

    List<Recruitment> getBatchRecruitmentList(RecruitmentStatus recruitmentStatus);
}
