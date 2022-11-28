package moska.rebora.User.Service;

import moska.rebora.Common.BaseResponse;
import moska.rebora.User.DTO.MypageInfoDto;
import moska.rebora.User.DTO.MypageUpdateDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.sql.SQLIntegrityConstraintViolationException;

public interface MypageService {

    MypageInfoDto info(@Param("userEmail") String userEmail);

    Page<UserRecruitmentListDto> getParticipationHistory(Pageable pageable);

    Page<UserRecruitmentListDto> getMyRecruiter(Pageable pageable);

    void updatePushYn(@Param("userId") Long userId,
                      @Param("userPushYn") Boolean userPushYn,
                      @Param("userEmail") String userEmail);

    void updatePushNightYn(@Param("userId") Long userId,
                           @Param("userPushNightYn") Boolean userPushNightYn,
                           @Param("userEmail") String userEmail);

    BaseResponse changeMyInfo(Long userId, String userEmail, MypageUpdateDto mypageUpdateDto) throws SQLIntegrityConstraintViolationException;
}
