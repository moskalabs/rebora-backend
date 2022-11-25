package moska.rebora;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

@SpringBootApplication
@EnableJpaAuditing
@EnableBatchProcessing
@EnableScheduling
@EnableAsync
@Slf4j
public class ReboraApplication {

    @PostConstruct
    public void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        SpringApplication.run(ReboraApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return Optional.of("kkp02052@gmail.com");
            } else {
                return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
            }
        };
    }

    @Bean
    Hibernate5Module hibernate5Module() {
        Hibernate5Module hibernate5Module = new Hibernate5Module();
        //강제 지연 로딩 설정
        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
        return hibernate5Module;
    }

//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return () -> Optional.of(UUID.randomUUID().toString());
//    }
}
