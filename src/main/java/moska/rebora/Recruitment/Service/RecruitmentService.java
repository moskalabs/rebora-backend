package moska.rebora.Recruitment.Service;

import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Payment.Dto.ReserveRecruitmentDto;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public interface RecruitmentService {

    BasePageResponse<UserRecruitmentListDto> getList(Pageable pageable,
                                                     String userEmail,
                                                     UserSearchCondition userSearchCondition);

    BaseInfoResponse<RecruitmentInfoDto> getRecruitmentInfo(
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("commentPageable") Pageable commentPageable
    );

    Recruitment createRecruitment(
            @Param("userRecruitmentPeople") Integer userRecruitmentPeople,
            @Param("userEmail") String userEmail,
            @Param("movieId") Long movieId,
            @Param("theaterId") Long theaterId,
            @Param("recruitmentIntroduce") String recruitmentIntroduce,
            @Param("bannerYn") Boolean bannerYn,
            @Param("bannerSubText") String bannerSubText,
            @Param("bannerMainText") String bannerMainText,
            @Param("recruitmentCommentUseYn") Boolean recruitmentCommentUseYn,
            @Param("merchantUid") String merchantUid,
            @Param("impUid") String impUid
    );

//    ReserveRecruitmentDto reserveRecruitment(@Param("movieId") Long movieId,
//                                             @Param("theaterId") Long theaterId,
//                                             @Param("userEmail") String userEmail,
//                                             @Param("recruitmentIntroduce") String recruitmentIntroduce,
//                                             @Param("bannerYn") Boolean bannerYn,
//                                             @Param("bannerSubText") String bannerSubText,
//                                             @Param("bannerMainText") String bannerMainText);

    void cancelReserve(@Param("recruitmentId") Long recruitmentId);

    void applyRecruitment(@Param("recruitmentId") Long recruitmentId,
                          @Param("userEmail") String userEmail,
                          @Param("userRecruitmentPeople") Integer userRecruitmentPeople);

    void cancelRecruitment(@Param("recruitmentId") Long recruitmentId, @Param("userEmail") String userEmail);

    void updateRecruitment(
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("recruitmentIntroduce") String recruitmentIntroduce,
            @Param("bannerYn") Boolean bannerYn,
            @Param("bannerSubText") String bannerSubText,
            @Param("bannerMainText") String bannerMainText,
            @Param("recruitmentCommentUseYn") Boolean recruitmentCommentUseYn);
}
