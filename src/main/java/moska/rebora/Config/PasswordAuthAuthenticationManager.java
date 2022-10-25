package moska.rebora.Config;

import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class PasswordAuthAuthenticationManager implements AuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = userRepository.getUserByUserEmail(authentication.getPrincipal().toString());
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호 오류입니다.");
        }

        PasswordAuthAuthenticationToken token = new PasswordAuthAuthenticationToken(user.getUserEmail(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("role")));

        token.setId(user.getId());
        token.setRole("role");
        token.setUserEmail(user.getUserEmail());
        token.setUserName(user.getUserName());
        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PasswordAuthAuthenticationToken.class);
    }
}
