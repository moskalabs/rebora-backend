package moska.rebora.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    JwtAuthTokenProvider jwtAuthTokenProvider;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
                .cors().and()
                .headers().frameOptions().disable().and()
                .authorizeRequests()
                .antMatchers("/api/user/login").permitAll()
                .antMatchers("/api/user/getUserAuthenticated").permitAll()
                .antMatchers("/api/policy/**").permitAll()
                .antMatchers("/api/user/signUp").permitAll()
                .antMatchers("/api/user/sendVerificationEmail").permitAll()
                .antMatchers("/api/user/checkRedundancyNickname").permitAll()
                .antMatchers("/api/user/changePassword").permitAll()
                .antMatchers("/api/user/validationEmailCode").permitAll()
                .antMatchers("/api/user/oath/**").permitAll()
                .antMatchers("/api/payment/applyCard").permitAll()
                .antMatchers("/api/payment/applyDone").permitAll()
                .antMatchers("/api/payment/applyCardSuccess").permitAll()
                .antMatchers("/api/common/**").permitAll()
                .antMatchers("/api/movie/**").permitAll()
                .antMatchers("/api/main").permitAll()
                .antMatchers("/api/recruitment/info/**").permitAll()
                .antMatchers("/api/recruitment/getListByMovie/**").permitAll()
                .antMatchers("/api/recruitment/getSearchList/**").permitAll()
                .antMatchers("/api/recruitment/getList/**").permitAll()
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/recruitment/**").authenticated()
                .antMatchers("/api/comment/**").authenticated()
                .antMatchers("/api/theater/**").authenticated()
                .antMatchers("/api/wish/**").authenticated()
                .antMatchers("/api/payment/**").authenticated()
                .antMatchers("/admin/adminCheck").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .anyRequest().permitAll().and()
                .addFilterBefore(new JwtFilter(jwtAuthTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
