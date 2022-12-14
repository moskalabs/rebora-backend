package moska.rebora.User.Repository;

import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserImageListDto;
import moska.rebora.User.DTO.UserRecruitmentDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

public interface UserRecruitmentCustom {

    Long countParticipationHistory(@Param("userEmail") @Valid String userEmail);

    Long countMyRecruiter(@Param("userEmail") @Valid String userEmail);

    Page<UserRecruitmentListDto> getUserRecruitmentList(@Param("userEmail") @Valid String userEmail,
                                                        Pageable pageable, UserSearchCondition userSearchCondition);

    List<UserImageListDto> getUserImageListByRecruitment(@Param("userEmail") @Valid String userEmail,
                                                         @Param("recruitmentId") Long recruitmentId);

    List<UserRecruitment> getUserRecruitmentByRecruitment(Recruitment recruitment);

    List<UserRecruitment> getBatchRefundUserRecruitment(Long recruitmentId);

    List<UserRecruitment> getBatchUserWishRecruitment(Long recruitmentId);
}
