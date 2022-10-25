package moska.rebora.User.Service;

import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        User user = userRepository.getUserById(Long.valueOf(username));
        return org.springframework.security.core.userdetails.User.builder().username(String.valueOf(user.getId())).build();
    }
}
