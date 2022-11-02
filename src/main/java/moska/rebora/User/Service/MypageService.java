package moska.rebora.User.Service;

import moska.rebora.User.DTO.UserRecruitmentListDto;
import net.minidev.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface MypageService {

    JSONObject info(@Param("userEmail") String userEmail);

    Page<UserRecruitmentListDto> getParticipationHistory(Pageable pageable);

    Page<UserRecruitmentListDto> getMyRecruiter(Pageable pageable);
}
