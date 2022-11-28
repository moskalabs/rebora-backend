package moska.rebora.User.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import moska.rebora.User.DTO.UserLoginDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class OathServiceImpl implements OathService{

    @Value("${sns.login.naver.key}")
    private String NAVER_KEY;

    @Value("${sns.login.naver.key}")
    private String NAVER_CLIENT_ID;

    private RestTemplate restTemplate;

    @Override
    public UserLoginDto naverLogin(String code, String callbackUrl, String state) {

        ObjectMapper objectMapper = new ObjectMapper();
    }
}
