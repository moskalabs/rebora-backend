package moska.rebora.User.Controller;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.User.DTO.MypageUpdateDto;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserRecruitmentDtoListResponse;
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

@RestController
@RequestMapping("/api/user/mypage")
@Slf4j
public class MypageController {

    @Autowired
    MypageService mypageService;

    @Autowired
    UserService userService;

    /**
     * 마이페이지 개수 가져오기
     *
     * @return JSONObject
     */
    @GetMapping("/info")
    public JSONObject info() {
        return mypageService.info(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * 내가 참여한 모집 내역 가져오기
     *
     * @param pageable 페이징
     * @return UserRecruitmentDtoListResponse
     */
    @GetMapping("/getParticipationHistory")
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
    @GetMapping("/getMyRecruiter")
    public UserRecruitmentDtoListResponse getMyRecruiter(Pageable pageable) {

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
    @PutMapping("/updatePushYn/{userId}")
    public BaseResponse updatePushYn(@PathVariable Long userId, @RequestParam("userPushYn") Boolean userPushYn) {
        BaseResponse baseResponse = new BaseResponse();
        mypageService.updatePushYn(userId, userPushYn, SecurityContextHolder.getContext().getAuthentication().getName());
        baseResponse.setResult(true);
        return baseResponse;
    }

    @PutMapping("/updatePushNightYn/{userId}")
    public BaseResponse updatePushNightYn(@PathVariable Long userId, @RequestParam("userPushYn") Boolean userPushNightYn){
        BaseResponse baseResponse = new BaseResponse();
        mypageService.updatePushNightYn(userId, userPushNightYn, SecurityContextHolder.getContext().getAuthentication().getName());
        baseResponse.setResult(true);
        return baseResponse;
    }

    /**
     * 마이페이지 정보 가져오기
     *
     * @return getMyInfo
     */
    @GetMapping("/getMyInfo")
    public UserDto getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = new UserDto(userService.getUserInfoByUserEmail(authentication.getName()));
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
    @PutMapping("/changeMyInfo/{userId}")
    public BaseResponse changeMyInfo(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "", required = false) String userNickname,
                                     @RequestParam(defaultValue = "", required = false) String currentPassword,
                                     @RequestParam(defaultValue = "", required = false) String changePassword,
                                     @RequestParam(defaultValue = "", required = false) MultipartFile file
    ) throws SQLIntegrityConstraintViolationException {
        MypageUpdateDto mypageUpdateDto = new MypageUpdateDto();
        mypageUpdateDto.setFile(file);
        mypageUpdateDto.setChangePassword(changePassword);
        mypageUpdateDto.setCurrentPassword(currentPassword);
        mypageUpdateDto.setUserNickname(userNickname);
        return mypageService.changeMyInfo(userId, SecurityContextHolder.getContext().getAuthentication().getName(), mypageUpdateDto);
    }
}
