package moska.rebora.Recruitment.Controller;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Service.RecruitmentService;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api/recruitment")
@Slf4j
public class RecruitmentController {

    @Autowired
    RecruitmentService recruitmentService;

    /**
     * 모집리스트 가져오기
     *
     * @param pageable          페이징
     * @param theaterRegion     극장지역
     * @param recruitmentStatus 모집 상태
     * @return BasePageResponse<UserRecruitmentListDto>
     */
    @GetMapping("/getList")
    public BasePageResponse<UserRecruitmentListDto> getList(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false, defaultValue = "all") String theaterRegion,
            @RequestParam(required = false, defaultValue = "all") String recruitmentStatus
    ) {
        log.info("theaterRegion={}", theaterRegion);
        log.info("recruitmentStatus={}", recruitmentStatus);

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();

        if (!theaterRegion.equals("all") && hasText(theaterRegion)) {
            userSearchCondition.setTheaterRegion(theaterRegion);
        }

        if (!recruitmentStatus.equals("all") && hasText(recruitmentStatus)) {
            userSearchCondition.setRecruitmentStatus(RecruitmentStatus.valueOf(recruitmentStatus));
        }

        return recruitmentService.getList(pageable, userEmail, userSearchCondition);
    }

    /**
     * 영화로 모집 리스트 가져오기
     *
     * @param movieId  영화 아이디
     * @param pageable 페이징
     * @return BasePageResponse<UserRecruitmentListDto>
     */
    @GetMapping("/getListByMovie/{movieId}")
    public BasePageResponse<UserRecruitmentListDto> getListByMovie(
            @PathVariable Long movieId,
            @PageableDefault(page = 0, size = 10) Pageable pageable

    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setMovieId(movieId);
        userSearchCondition.setRecruitmentStatus(RecruitmentStatus.RECRUITING);
        return recruitmentService.getList(pageable, userEmail, userSearchCondition);
    }

    /**
     * 검색 모집 리스트 가져오기
     *
     * @param searchWord 검색어
     * @param pageable   페이징
     * @return BasePageResponse<UserRecruitmentListDto>
     */
    @GetMapping("/searchList")
    public BasePageResponse<UserRecruitmentListDto> searchList(
            @RequestParam(required = false, defaultValue = "") String searchWord,
            @PageableDefault(page = 0, size = 10) Pageable pageable

    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();

        if (hasText(searchWord)) {
            userSearchCondition.setSearchWord(searchWord);
        }

        userSearchCondition.setRecruitmentStatus(RecruitmentStatus.RECRUITING);
        return recruitmentService.getList(pageable, userEmail, userSearchCondition);
    }

    /**
     * 모집 정보 가져오기
     *
     * @param recruitmentId 모집 아이디
     * @return BaseInfoResponse<RecruitmentInfoDto>
     */
    @GetMapping("/info/{recruitmentId}")
    public BaseInfoResponse<RecruitmentInfoDto> info(@PathVariable Long recruitmentId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable commentPageable = PageRequest.of(0, 10);
        return recruitmentService.getRecruitmentInfo(recruitmentId, userEmail, commentPageable);
    }
}
