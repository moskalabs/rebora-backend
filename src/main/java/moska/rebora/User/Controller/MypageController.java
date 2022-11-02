package moska.rebora.User.Controller;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.User.DTO.UserRecruitmentDtoListResponse;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.Service.MypageService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/mypage")
@Slf4j
public class MypageController {

    @Autowired
    MypageService mypageService;

    @GetMapping("/info")
    public JSONObject info() {
        return mypageService.info(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/getParticipationHistory")
    public UserRecruitmentDtoListResponse getParticipationHistory(Pageable pageable) {

        UserRecruitmentDtoListResponse userRecruitmentDtoListResponse = new UserRecruitmentDtoListResponse();
        userRecruitmentDtoListResponse.setResult(true);
        userRecruitmentDtoListResponse.setUserRecruitmentList(mypageService.getParticipationHistory(pageable));
        return userRecruitmentDtoListResponse;
    }

    @GetMapping("/getMyRecruiter")
    public UserRecruitmentDtoListResponse getMyRecruiter(Pageable pageable) {

        UserRecruitmentDtoListResponse userRecruitmentDtoListResponse = new UserRecruitmentDtoListResponse();
        userRecruitmentDtoListResponse.setResult(true);
        userRecruitmentDtoListResponse.setUserRecruitmentList(mypageService.getMyRecruiter(pageable));
        return userRecruitmentDtoListResponse;
    }
}
