package moska.rebora.Common.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticateTokenImpl implements AuthenticateToken{

    @Value("${import.auth.imp_key}")
    private String ImpKey;

    @Value("${import.auth.imp_secret}")
    private String ImpSecret;

    @Override
    public String getAuthenticateToken() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> params = new HashMap<>();
            params.put("imp_key", ImpKey);
            params.put("imp_secret", ImpSecret);
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.iamport.kr/users/getToken"))
                    .header("Content-Type", "application/json")
                    .method("POST", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.body());
            JSONObject responseJson = (JSONObject) jsonObj.get("response");
            return String.valueOf(responseJson.get("access_token"));
        } catch (ParseException | IOException | InterruptedException e) {
            throw new RuntimeException("카드 정보를 가져오던 도중 오류가 발생했습니다 다시 시도해 주세요");
        }
    }
}
