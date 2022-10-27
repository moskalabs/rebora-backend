package moska.rebora.User.Service;

import moska.rebora.User.Repository.UserRecruitmentRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MypageServiceImpl implements MypageService{

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Override
    public JSONObject info(@Param("userEmail") String userEmail) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("result", true);
        jsonObject.put("countParticipationHistory", userRecruitmentRepository.countParticipationHistory(userEmail));
        jsonObject.put("countMyRecruiter", userRecruitmentRepository.countMyRecruiter(userEmail));
        return jsonObject;
    }
}
