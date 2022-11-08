package moska.rebora.User.Controller;

import moska.rebora.Enum.PolicySubject;
import moska.rebora.User.Repository.PolicyRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {

    @Autowired
    PolicyRepository policyRepository;

    @GetMapping("/getPolicy")
    JSONObject getPolicy(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        jsonObject.put("termsCondition", policyRepository.getFirstByPolicySubjectOrderByRegDateDesc(PolicySubject.TERMS_CONDITION));
        jsonObject.put("privacyPolicy", policyRepository.getFirstByPolicySubjectOrderByRegDateDesc(PolicySubject.PRIVACY_POLICY));
        jsonObject.put("eventMarketing", policyRepository.getFirstByPolicySubjectOrderByRegDateDesc(PolicySubject.EVENT_MARKETING));

        return jsonObject;
    }
}
