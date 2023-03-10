package moska.rebora.User.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.User.DTO.*;
import moska.rebora.User.Service.MypageService;
import moska.rebora.User.Service.UserService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;

@Tag(name = "마이페이지")
@RestController
@RequestMapping("/api/user/mypage")
@Slf4j
@AllArgsConstructor
public class MypageController {

    MypageService mypageService;
    UserService userService;

    /**
     * 마이페이지 메인화면 정보
     *
     * @return JSONObject
     */
    @Tag(name = "마이페이지")
    @Operation(summary = "마이페이지지 메인 정보 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "마이페이지 정보 가져오기 성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/info")
    public MypageInfoDto info() {
        return mypageService.
                info(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * 내가 참여한 모집 내역 가져오기
     *
     * @param pageable 페이징
     * @return UserRecruitmentDtoListResponse
     */
    @Tag(name = "마이페이지")
    @Operation(summary = "내가 참여한 모집 리스트 가져오기")
    @GetMapping("/getParticipationHistory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public UserRecruitmentDtoListResponse getParticipationHistory(Pageable pageable) {
        UserRecruitmentDtoListResponse userRecruitmentDtoListResponse = new UserRecruitmentDtoListResponse();
        userRecruitmentDtoListResponse.setResult(true);
        userRecruitmentDtoListResponse.setUserRecruitmentList(mypageService.getParticipationHistory(pageable));
        return userRecruitmentDtoListResponse;
    }

    /**
     * 내가 모집한 모집글 가져오기
     *
     * @param pageable 페이징
     * @return UserRecruitmentDtoListResponse
     */
    @Tag(name = "마이페이지")
    @Operation(summary = "내가 모집한 모집 리스트 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getMyRecruiter")
    public UserRecruitmentDtoListResponse getMyRecruiter(
            Pageable pageable
    ) {

        UserRecruitmentDtoListResponse userRecruitmentDtoListResponse = new UserRecruitmentDtoListResponse();
        userRecruitmentDtoListResponse.setResult(true);
        userRecruitmentDtoListResponse.setUserRecruitmentList(mypageService.getMyRecruiter(pageable));
        return userRecruitmentDtoListResponse;
    }

    /**
     * 유저 푸쉬 변경
     *
     * @param userId     유저 아이디
     * @param userPushYn 유저 푸쉬 여부
     * @return BaseResponse
     */
    @Tag(name = "마이페이지")
    @PutMapping("/updatePushYn/{userId}")
    @Operation(summary = "푸쉬 여부 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저 아이디", required = true),
            @ApiImplicitParam(name = "userPushYn", value = "유저 푸쉬 여부", required = true),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "푸쉬 여부 변경 성공", content = @Content(schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    public BaseResponse updatePushYn(
            @PathVariable Long userId,
            @RequestParam Boolean userPushYn,
            @RequestParam(required = false, defaultValue = "") String userPushKey
    ) {
        BaseResponse baseResponse = new BaseResponse();
        mypageService.updatePushYn(userId, userPushYn, SecurityContextHolder.getContext().getAuthentication().getName(), userPushKey);
        baseResponse.setResult(true);
        return baseResponse;
    }

    /**
     * 야간 푸쉬 여부 업데이트
     *
     * @param userId          유저 아이디
     * @param userPushNightYn 유저 야간 푸쉬 여부
     * @param userPushKey     유저 푸쉬 키
     * @return BaseResponse
     */
    @Tag(name = "마이페이지")
    @Operation(summary = "야간 푸쉬 여부 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저 아이디", required = true),
            @ApiImplicitParam(name = "userPushNightYn", value = "유저 푸쉬 여부", required = true),
            @ApiImplicitParam(name = "userPushKey", value = "유저 푸쉬 키", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/updatePushNightYn/{userId}")
    public BaseResponse updatePushNightYn(
            @PathVariable Long userId,
            @RequestParam Boolean userPushNightYn,
            @RequestParam(required = false, defaultValue = "") String userPushKey
    ) {
        BaseResponse baseResponse = new BaseResponse();
        mypageService.updatePushNightYn(userId, userPushNightYn, SecurityContextHolder.getContext().getAuthentication().getName(), userPushKey);
        baseResponse.setResult(true);
        return baseResponse;
    }

    /**
     * 마이페이지 정보 가져오기
     *
     * @return getMyInfo
     */
    @Tag(name = "마이페이지")
    @Operation(summary = "마이페이지 내 정보 가져오기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getMyInfo")
    public UserDto getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.getUserInfoByUserEmail(authentication.getName());
        userDto.setResult(true);
        return userDto;
    }

    /**
     * 내정보 변경
     *
     * @param userId          유저 아이디
     * @param userNickname    유저 닉네임
     * @param currentPassword 현재 비밀번호
     * @param changePassword  변경 비밀번호
     * @param file            유저 파일
     * @return BaseResponse
     * @throws SQLIntegrityConstraintViolationException 중복일 경우 Exception
     */
    @Tag(name = "마이페이지")
    @Operation(summary = "유저 정보 업데이트")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "유저 아이디"),
            @ApiImplicitParam(name = "userNickname", value = "유저 닉네임", required = false),
            @ApiImplicitParam(name = "currentPassword", value = "현재 비밀번호", required = false),
            @ApiImplicitParam(name = "changePassword", value = "비밀번호 변경", required = false),
            @ApiImplicitParam(name = "file", value = "유저 이미지", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/changeMyInfo/{userId}")
    public BaseResponse changeMyInfo(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "", required = false) String userNickname,
                                     @RequestParam(defaultValue = "", required = false) String currentPassword,
                                     @RequestParam(defaultValue = "", required = false) String changePassword,
                                     @RequestParam(required = false) MultipartFile file
    ) throws SQLIntegrityConstraintViolationException {
        
        if (userNickname == null) {
            userNickname = "";
        }

        if (currentPassword == null) {
            currentPassword = "";
        }

        if (changePassword == null) {
            changePassword = "";
        }

        MypageUpdateDto mypageUpdateDto = new MypageUpdateDto();
        mypageUpdateDto.setFile(file);
        mypageUpdateDto.setChangePassword(changePassword);
        mypageUpdateDto.setCurrentPassword(currentPassword);
        mypageUpdateDto.setUserNickname(userNickname);
        return mypageService.changeMyInfo(userId, SecurityContextHolder.getContext().getAuthentication().getName(), mypageUpdateDto);
    }
}


