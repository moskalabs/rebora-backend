package moska.rebora;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
public class DateTest {

    @Test
    public void dateTest(){
        LocalDate localDate = LocalDate.now().minusYears(19L);
        log.info("localDate={}", localDate);
    }
}
