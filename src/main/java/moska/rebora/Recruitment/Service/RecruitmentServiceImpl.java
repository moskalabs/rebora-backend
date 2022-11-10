package moska.rebora.Recruitment.Service;

import moska.rebora.Comment.Repository.CommentRepository;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class RecruitmentServiceImpl implements RecruitmentService {

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Override
    public BasePageResponse<UserRecruitmentListDto> getList(Pageable pageable, String userEmail, UserSearchCondition userSearchCondition) {

        BasePageResponse<UserRecruitmentListDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setPage(recruitmentRepository.getList(pageable, userEmail, userSearchCondition));
        basePageResponse.setResult(true);

        return basePageResponse;
    }

    @Override
    public BaseInfoResponse<RecruitmentInfoDto> getRecruitmentInfo(
            @Param("recruitmentId") Long recruitmentId,
            @Param("userEmail") String userEmail,
            @Param("commentPageable") Pageable commentPageable
    ) {

        BaseInfoResponse<RecruitmentInfoDto> baseInfoResponse = new BaseInfoResponse<>();
        baseInfoResponse.setResult(true);
        RecruitmentInfoDto recruitmentInfoDto = recruitmentRepository.getRecruitmentInfo(recruitmentId, userEmail);
        recruitmentInfoDto.addPageComment(commentRepository.getCommentPage(commentPageable, recruitmentId));
        recruitmentInfoDto.addUserImageList(userRecruitmentRepository.getUserImageListByRecruitment(userEmail, recruitmentId));
        baseInfoResponse.setContent(recruitmentInfoDto);

        return baseInfoResponse;
    }

    @Override
    public void createRecruitment(Long movieId, Long theaterId, String userEmail, String recruitmentIntroduce, Integer userRecruitmentPeople) {

    }
}
