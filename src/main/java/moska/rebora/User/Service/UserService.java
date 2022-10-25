package moska.rebora.User.Service;

import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface UserService {

    UserLoginDto login(@Param("userEmail") String userEmail,
                       @Param("password") String password);

    UserLoginDto signUp(@Param("userEmail") String userEmail,
                        @Param("password") String password,
                        @Param("userName") String userName,
                        @Param("userNickname") String userNickname,
                        @Param("userPushYn") Boolean userPushYn,
                        @Param("userPushKey") String userPushKey
    );
}
