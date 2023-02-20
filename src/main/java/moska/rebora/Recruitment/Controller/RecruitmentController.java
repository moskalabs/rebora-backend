package moska.rebora.Recruitment.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Payment.Dto.ReserveRecruitmentDto;
import moska.rebora.Recruitment.Dto.CreateRecruitmentDto;
import moska.rebora.Recruitment.Dto.RecruitmentInfoDto;
import moska.rebora.Recruitment.Entity.Recruitment;
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
@AllArgsConstructor
@RequestMapping("/api/recruitment")
@Slf4j
@Tag(name = "모집")
public class RecruitmentController {

    RecruitmentService recruitmentService;

    /**
     * 모집리스트 가져오기
     *
     * @param pageable          페이징
     * @param theaterRegion     극장지역
     * @param recruitmentStatus 모집 상태
     * @return BasePageResponse<UserRecruitmentListDto>
     */
    @Tag(name = "모집")
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
    @Tag(name = "모집")
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
    @Tag(name = "모집")
    @GetMapping("/getSearchList")
    public BasePageResponse<UserRecruitmentListDto> getSearchList(
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
    @Tag(name = "모집")
    @GetMapping("/info/{recruitmentId}")
    public BaseInfoResponse<RecruitmentInfoDto> info(
            @PathVariable Long recruitmentId
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable commentPageable = PageRequest.of(0, 10);
        return recruitmentService.getRecruitmentInfo(recruitmentId, userEmail, commentPageable);
    }

    /**
     * 모집 생성
     *
     * @param userRecruitmentPeople 모집 신청 인원
     * @param movieId               영화 아이디
     * @param theaterId             상영관 아이디
     * @param recruitmentIntroduce  모집 소개
     * @param bannerYn              배너 유무
     * @param bannerSubText         배너 서브 텍스트
     * @param bannerMainText        배너 메인 텍스트
     * @param merchantUid           상품 유아이디
     * @param impUid                결제 유아이디
     * @return BaseInfoResponse<ReserveRecruitmentDto>
     */
    @Tag(name = "모집")
    @PostMapping("/createRecruitment")
    public BaseInfoResponse<CreateRecruitmentDto> createRecruitment(
            @RequestParam Integer userRecruitmentPeople,
            @RequestParam Long movieId,
            @RequestParam Long theaterId,
            @RequestParam("recruitmentIntroduce") String recruitmentIntroduce,
            @RequestParam("bannerYn") Boolean bannerYn,
            @RequestParam(value = "bannerSubText", required = false) String bannerSubText,
            @RequestParam(value = "bannerMainText", required = false) String bannerMainText,
            @RequestParam(value = "recruitmentCommentUseYn", required = false, defaultValue = "true") Boolean recruitmentCommentUseYn,
            @RequestParam String merchantUid,
            @RequestParam String impUid
    ) {

        log.info("모집 생성 Request userRecruitmentPeople={} movieId={} theaterId={} recruitmentIntroduce={} bannerYn={} bannerSubText={} bannerMainText={} merchantUid={} impUid={} recruitmentCommentUseYn={}",
                userRecruitmentPeople, movieId, theaterId, recruitmentIntroduce, bannerYn, bannerSubText, bannerMainText, merchantUid, impUid, recruitmentCommentUseYn);

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        BaseInfoResponse<CreateRecruitmentDto> baseInfoResponse = new BaseInfoResponse<>();
        CreateRecruitmentDto createRecruitmentDto = new CreateRecruitmentDto();

        Recruitment recruitment = recruitmentService.createRecruitment(userRecruitmentPeople, userEmail, movieId, theaterId, recruitmentIntroduce, bannerYn, bannerSubText, bannerMainText, recruitmentCommentUseYn, merchantUid, impUid);

        baseInfoResponse.setResult(true);
        createRecruitmentDto.setRecruitmentId(recruitment.getId());
        baseInfoResponse.setContent(createRecruitmentDto);

        return baseInfoResponse;
    }

    /**
     * 모집 취소
     *
     * @param recruitmentId 모집 아이디
     * @return BaseResponse
     */
    @PostMapping("/cancelReserve/{recruitmentId}")
    public BaseResponse cancelReserve(
            @PathVariable Long recruitmentId
    ) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        recruitmentService.cancelReserve(recruitmentId);
        return baseResponse;
    }

    /**
     * 모집 신청
     *
     * @param recruitmentId         모집 PK
     * @param userRecruitmentPeople 모집 신청 인원
     * @return BaseResponse
     */
    @Tag(name = "모집")
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
    @Tag(name = "모집")
    @PutMapping("/cancelRecruitment/{recruitmentId}")
    public BaseResponse cancelRecruitment(@PathVariable Long recruitmentId) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        recruitmentService.cancelRecruitment(recruitmentId, userEmail);
        return baseResponse;
    }

    /**
     * 모집 업데이트
     *
     * @param recruitmentId           모집 아이디
     * @param recruitmentIntroduce    모집 소개
     * @param bannerYn                배너 유무
     * @param bannerSubText           배너 서브 텍스트
     * @param bannerMainText          배너 메인 텍스트
     * @param recruitmentCommentUseYn 모집 댓글 사용 여부
     * @return BaseResponse
     */
    @PutMapping("/updateRecruitment/{recruitmentId}")
    public BaseResponse updateRecruitment(
            @PathVariable Long recruitmentId,
            @RequestParam("recruitmentIntroduce") String recruitmentIntroduce
    ) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        recruitmentService.updateRecruitment(recruitmentId, userEmail, recruitmentIntroduce);

        return baseResponse;
    }

    @PutMapping("/updateRecruitmentCommentUse/{recruitmentId}")
    public BaseResponse updateRecruitmentCommentUse(
            @PathVariable Long recruitmentId,
            @RequestParam Boolean recruitmentCommentUseYn
    ) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        recruitmentService.updateRecruitmentCommentUse(recruitmentId, userEmail, recruitmentCommentUseYn);

        return baseResponse;
    }

}
