package moska.rebora.User.Controller;

import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Service.OathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static moska.rebora.Common.CommonConst.CURRENT_NAVER_CALLBACK;

@RestController
@RequestMapping("/api/user/oath")
@Slf4j
public class OathController {

    @Value("${sns.login.naver.key}")
    private String NAVER_KEY;

    @Value("${sns.login.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Autowired
    OathService oathService;

    @GetMapping("/naverLogin")
    public ModelAndView naverLogin(ModelAndView mav) {

        mav.addObject("NAVER_KEY", NAVER_KEY);
        mav.addObject("NAVER_CLIENT_ID", NAVER_CLIENT_ID);
        mav.addObject("CALLBACK_URL", CURRENT_NAVER_CALLBACK);
        mav.setViewName("/oath/naverLogin");

        return mav;
    }

    @GetMapping("/naverCallback")
    public ModelAndView naverCallback(
            ModelAndView mav,
            @RequestParam String code,
            @RequestParam String state) throws Exception {

        String callbackUrl = CURRENT_NAVER_CALLBACK;
        UserLoginDto userLoginDto = oathService.login(code, callbackUrl, state, UserSnsKind.NAVER);
        mav.setViewName("/oath/naverCallback");
        mav.addObject("userLoginDto", userLoginDto);

        return mav;
    }
}
