package moska.rebora;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.Service.AuthenticateToken;
import moska.rebora.User.DTO.UserAuthenticatedDto;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static moska.rebora.Common.CommonConst.RESPONSE_OK;

@Slf4j
@SpringBootTest
public class AuthenticatedTest {

    @Autowired
    AuthenticateToken authenticateToken;

    @Test
    public void certifications() throws IOException, InterruptedException, ParseException {
        String token = authenticateToken.getAuthenticateToken();
        String impUid = "imp_934749702710";
        log.info("hello token={}", token);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        params.put("imp_uid", impUid);

        String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.iamport.kr/certifications/"+impUid))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .method("GET", body)
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser jsonParser = new JSONParser();

        response.statusCode();

        if(response.statusCode() == RESPONSE_OK){

            JSONObject result = (JSONObject) jsonParser.parse(response.body());
            Long code = (Long) result.get("code");
            String message = (String) result.get("message"); //에러 메세지
            JSONObject jsonObject = (JSONObject) result.get("response");

            UserAuthenticatedDto userAuthenticatedDto = new UserAuthenticatedDto(jsonObject);
        }
    }
}
