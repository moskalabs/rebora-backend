package moska.rebora.Recruitment.Controller;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Service.RecruitmentService;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * 모집 개시
     *
     * @param movieId               영화 PK
     * @param theaterId             상영관 PK
     * @param recruitmentIntroduce  모집 소개글
     * @param userRecruitmentPeople 모집 신청 인원
     * @param bannerYn              배너 유무
     * @param bannerSubText         배너 서브 텍스트
     * @param bannerMainText        배너 메인 텍스트
     * @return JSONObject
     */
    @PostMapping("/createRecruitment")
    public JSONObject createRecruitment(
            @RequestParam Long movieId,
            @RequestParam Long theaterId,
            @RequestParam("recruitmentIntroduce") String recruitmentIntroduce,
            @RequestParam("userRecruitmentPeople") Integer userRecruitmentPeople,
            @RequestParam("bannerYn") Boolean bannerYn,
            @RequestParam(value = "bannerSubText", required = false) String bannerSubText,
            @RequestParam(value = "bannerMainText", required = false) String bannerMainText
    ) {
        JSONObject jsonObject = new JSONObject();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        jsonObject.put("result", true);
        jsonObject.put("recruitmentId", recruitmentService.createRecruitment(movieId, theaterId, userEmail, recruitmentIntroduce, userRecruitmentPeople, bannerYn, bannerSubText, bannerMainText));

        return jsonObject;
    }

    /**
     * 모집 신청
     *
     * @param recruitmentId         모집 PK
     * @param userRecruitmentPeople 모집 신청 인원
     * @return BaseResponse
     */
    @PostMapping("/applyRecruitment/{recruitmentId}")
    public BaseResponse applyRecruitment(
            @PathVariable Long recruitmentId,
            @RequestParam("userRecruitmentPeople") Integer userRecruitmentPeople
    ) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        recruitmentService.applyRecruitment(recruitmentId, userEmail, userRecruitmentPeople);

        return baseResponse;
    }

    /**
     * 모집 신청 취소
     *
     * @param recruitmentId 모집 PK
     * @return BaseResponse
     */
    @PutMapping("/cancelRecruitment/{recruitmentId}")
    public BaseResponse cancelRecruitment(@PathVariable Long recruitmentId) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        recruitmentService.cancelRecruitment(recruitmentId, userEmail);
        return baseResponse;
    }
}
