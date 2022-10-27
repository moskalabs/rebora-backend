package moska.rebora.User.Service;

import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

public interface MypageService {

    public JSONObject info(@Param("userEmail") String userEmail);
}
