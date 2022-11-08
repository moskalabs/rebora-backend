package moska.rebora.User.Service;

import io.jsonwebtoken.JwtException;
import moska.rebora.Common.BaseResponse;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MypageServiceImpl implements MypageService {

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * 마이페이지 정보 가져오기
     * 참여모집내역 개수, 내가 한 모집글 개수
     *
     * @param userEmail 유저 이메일
     * @return JSONObject
     */
    @Override
    public JSONObject info(@Param("userEmail") String userEmail) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("result", true);
        jsonObject.put("countParticipationHistory", userRecruitmentRepository.countParticipationHistory(userEmail));
        jsonObject.put("countMyRecruiter", userRecruitmentRepository.countMyRecruiter(userEmail));
        return jsonObject;
    }

    /**
     * 내가 참여한 모집 내역 가져오기
     *
     * @param pageable 페이징
     * @return UserRecruitmentListDto
     */
    @Override
    public Page<UserRecruitmentListDto> getParticipationHistory(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setTheaterStartDatetime(false);
        Page<UserRecruitmentListDto> userRecruitmentListDtoPage = userRecruitmentRepository.getUserRecruitmentList(userEmail, pageable, userSearchCondition);

        return userRecruitmentListDtoPage;
    }

    /**
     * 내가 모집한 모집글 가져오기
     *
     * @param pageable 페이징
     * @return Page<UserRecruitmentListDto>
     */
    @Override
    public Page<UserRecruitmentListDto> getMyRecruiter(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setCreateByMe(true);
        Page<UserRecruitmentListDto> userRecruitmentListDtoPage = userRecruitmentRepository.getUserRecruitmentList(userEmail, pageable, userSearchCondition);
        return userRecruitmentListDtoPage;
    }

    /**
     * 유저 푸쉬 변경
     *
     * @param userId     유저 아이디
     * @param userPushYn 유저 푸쉬 여부
     * @param userEmail  유저 이메일
     */
    @Override
    public void updatePushYn(@Param("userId") Long userId,
                             @Param("userPushYn") Boolean userPushYn,
                             @Param("userEmail") String userEmail) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new JwtException("옳바르지 않은 접근입니다.");
        }
        User user = userOptional.get();

        if (user.getUserEmail().equals(userEmail)) {
            user.changePushYn(userPushYn);
            userRepository.save(user);
        } else {
            throw new JwtException("옳바르지 않은 접근입니다.");
        }
    }
}
