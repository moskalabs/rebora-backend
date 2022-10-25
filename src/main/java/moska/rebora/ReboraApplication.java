package moska.rebora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class ReboraApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReboraApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
