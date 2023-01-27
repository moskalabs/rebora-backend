package moska.rebora.Common.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.Dto.FCMMessage;
import moska.rebora.User.Entity.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class PushServiceImpl implements PushService {

    static final String API_URL = "https://fcm.googleapis.com/v1/projects/rebora-98afa/messages:send";

    String getBearerFireBaseToken() throws IOException {
        String firebaseConfigPath = "static/rebora-98afa-firebase-adminsdk-4dgzs-196b51a679.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        String tokenValue = googleCredentials.getAccessToken().getTokenValue();
        log.info("token={}", tokenValue);

        return tokenValue;
    }

    @Override
    public void sendPush(String token, String title, String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        FCMMessage fcmMessage = FCMMessage.builder()
                .message(
                        FCMMessage.Message
                                .builder()
                                .notification(FCMMessage.Notification.builder()
                                        .title(title)
                                        .body(content)
                                        .image(null)
                                        .build())
                                .token(token)
                                .build()
                )
                .validate_only(false)
                .build();

        try {
            String requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fcmMessage);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Authorization", "Bearer " + getBearerFireBaseToken())
                    .header("Content-Type", "application/json; UTF-8")
                    .method("POST", body)
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(response.body());
        } catch (ParseException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendUserPush(User user, String title, String content) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() > 22 || now.getHour() < 7) {
            if (user.getUserUseYn() && user.getUserPushYn() && user.getUserPushNightYn() && user.getUserPushKey() != null) {
                sendPush(user.getUserPushKey(), title, content);
            }
        } else {
            if (user.getUserUseYn() && user.getUserPushYn() && user.getUserPushKey() != null) {
                sendPush(user.getUserPushKey(), title, content);
            }
        }
    }
}
