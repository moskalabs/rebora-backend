package moska.rebora.Recruitment.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "모집 리스트 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "theaterRegion", value = "상영 지역", required = false),
            @ApiImplicitParam(name = "recruitmentStatus", value = "모집 상태", required = false),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
    @Operation(summary = "모집 리스트 영화로 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "movieId", value = "영화 아이디", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
    @Operation(summary = "모집리스트 영화 이름 검색으로 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "movieId", value = "영화 아이디", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
    @Operation(summary = "모집 정보 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
    @Operation(summary = "모집 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userRecruitmentPeople", value = "모집 신청 인원", required = true),
            @ApiImplicitParam(name = "movieId", value = "영화 아아디", required = true),
            @ApiImplicitParam(name = "theaterId", value = "상영관 아이디", required = true),
            @ApiImplicitParam(name = "recruitmentIntroduce", value = "모집 소개글", required = true),
            @ApiImplicitParam(name = "bannerYn", value = "모집 배너 여부", required = true),
            @ApiImplicitParam(name = "bannerSubText", value = "모집 서브텍스트", required = true),
            @ApiImplicitParam(name = "bannerMainText", value = "모집 메인 텍스트", required = true),
            @ApiImplicitParam(name = "recruitmentCommentUseYn", value = "모집 댓글 사용 여부", required = true),
            @ApiImplicitParam(name = "merchantUid", value = "주문 번호", required = true),
            @ApiImplicitParam(name = "impUid", value = "아임포트 uid", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
     * 모집 신청 취소
     *
     * @param recruitmentId 모집 PK
     * @return BaseResponse
     */
    @Tag(name = "모집")
    @Operation(summary = "모집 신청 취소")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
     * @return BaseResponse
     */
    @Tag(name = "모집")
    @Operation(summary = "모집 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true),
            @ApiImplicitParam(name = "recruitmentIntroduce", value = "모집 소개글", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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

    @Tag(name = "모집")
    @Operation(summary = "모집 댓글 변경 여부")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true),
            @ApiImplicitParam(name = "recruitmentCommentUseYn", value = "모집 댓글 여부", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
