package moska.rebora.Recruitment.Service;

import moska.rebora.Banner.Entity.Banner;
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

    void createRecruitment(
            @Param("recruitmentId") Long recruitmentId,
            @Param("userRecruitmentPeople") Integer userRecruitmentPeople,
            @Param("userEmail") String userEmail
    );

    Long reserveRecruitment(@Param("movieId") Long movieId,
                            @Param("theaterId") Long theaterId,
                            @Param("userEmail") String userEmail,
                            @Param("recruitmentIntroduce") String recruitmentIntroduce,
                            @Param("bannerYn") Boolean bannerYn,
                            @Param("bannerSubText") String bannerSubText,
                            @Param("bannerMainText") String bannerMainText);

    void cancelReserve(@Param("recruitmentId") Long recruitmentId);

    void applyRecruitment(@Param("recruitmentId") Long recruitmentId,
                          @Param("userEmail") String userEmail,
                          @Param("userRecruitmentPeople") Integer userRecruitmentPeople);

    void cancelRecruitment(@Param("recruitmentId") Long recruitmentId, @Param("userEmail") String userEmail);
}
