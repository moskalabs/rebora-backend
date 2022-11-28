package moska.rebora.User.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/user/oath")
@Slf4j
public class OathController {

    @Value("${sns.login.naver.key}")
    private String key;


    @GetMapping("/naverLogin")
    public ModelAndView naverLogin(ModelAndView mav){

        mav.setViewName("naverLogin");

        return mav;
    }
}
