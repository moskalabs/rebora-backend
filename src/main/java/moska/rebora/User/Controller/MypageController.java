package moska.rebora.User.Controller;

import moska.rebora.User.Service.MypageService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/mypage")
public class MypageController {

    @Autowired
    MypageService mypageService;

    @GetMapping("/info")
    public JSONObject info() {
        return mypageService.info(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
