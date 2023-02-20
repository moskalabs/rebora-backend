package moska.rebora;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
public class DateTest {

    @Test
    public void dateTest() {
        LocalDate localDate = LocalDate.now().minusYears(19L);
        log.info("localDate={}", localDate);
    }

    @Test
    public void userTest() {
        String user = "{\"name\":{\"firstName\":\"기봉\",\"lastName\":\"김\"},\"email\":\"kkp02052@gmail.com\"}";
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(user);
        JsonObject name = element.getAsJsonObject().get("name").getAsJsonObject();
        String email = element.getAsJsonObject().get("email").toString();
        String firstName = name.get("firstName").toString();
        String lastName = name.get("lastName").toString();
        log.info("element={}", element);
        log.info("name={}", name);
        log.info("email={}", email);
        log.info("firstName={}", firstName);
        log.info("firstName={}", lastName);
    }
}
