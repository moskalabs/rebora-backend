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
    JSONObject getPolicy(@RequestParam("policySubject") PolicySubject policySubject){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", true);
        jsonObject.put("policy", policyRepository.getFirstByPolicySubjectOrderByRegDateDesc(policySubject));

        return jsonObject;
    }
}
