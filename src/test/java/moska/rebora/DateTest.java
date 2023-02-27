package moska.rebora;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Util.UserUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DateTest {

    @Autowired
    RecruitmentRepository recruitmentRepository;

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

    @Test
    public void snsKind() {
        UserSnsKind userSnsKind = UserSnsKind.KAKAO;
        UserUtil userUtil = new UserUtil();
        log.info("snsKind={}", userUtil.convertSnsKind(userSnsKind));

        String hello = "hello";
    }

    @Test
    public void test() {
        String hello = "hello";
        log.info(solution(hello));
    }


    public String solution(String s) {
        char[] ch = s.toCharArray();
        Arrays.sort(ch);
        String answer = new String(ch);
        HashSet<Character> checker = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            if (!checker.add(s.charAt(i))) {
                log.info(String.valueOf(s.charAt(i)));
                // 1번째 문자의 중복 여부를 확인하는 코드
                answer = answer.replaceAll(Character.toString(s.charAt(i)), ""); // 종복 문자가 나오면 answer 에서 해당 문자를 제거하는 코드
            }
        }

        return answer;
    }


    @Test
    public void batchTest(){
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setRecruitmentStatus(RecruitmentStatus.CONFIRMATION);

        recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, userSearchCondition);
    }
}
