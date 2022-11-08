package moska.rebora.User.Service;

import moska.rebora.Common.BaseResponse;
import moska.rebora.User.DTO.MypageUpdateDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.SQLIntegrityConstraintViolationException;

public interface MypageService {

    JSONObject info(@Param("userEmail") String userEmail);

    Page<UserRecruitmentListDto> getParticipationHistory(Pageable pageable);

    Page<UserRecruitmentListDto> getMyRecruiter(Pageable pageable);

    void updatePushYn(Long userId, Boolean userPushYn, String userEmail);

    public BaseResponse changeMyInfo(Long userId,String userEmail, MypageUpdateDto mypageUpdateDto) throws SQLIntegrityConstraintViolationException;
}
