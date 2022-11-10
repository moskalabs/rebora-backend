package moska.rebora.Recruitment.Service;

import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface RecruitmentService {

    BasePageResponse<UserRecruitmentListDto> getList(Pageable pageable,
                                                            String userEmail,
                                                            UserSearchCondition userSearchCondition);

    BaseInfoResponse<RecruitmentInfoDto> getRecruitmentInfo(
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("commentPageable") Pageable commentPageable
    );

    void createRecruitment(Long movieId, Long theaterId, String userEmail, String recruitmentIntroduce, Integer userRecruitmentPeople);
}
