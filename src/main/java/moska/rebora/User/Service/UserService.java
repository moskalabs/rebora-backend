package moska.rebora.User.Service;

import moska.rebora.Common.BaseResponse;
import moska.rebora.Config.PasswordAuthAuthenticationToken;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.Valid;
import java.security.cert.CertificateExpiredException;
import java.sql.SQLIntegrityConstraintViolationException;

public interface UserService {

    UserLoginDto login(@Param("userEmail") String userEmail,
                       @Param("password") String password);

    UserLoginDto signUp(@Param("userEmail") String userEmail,
                        @Param("password") String password,
                        @Param("userName") String userName,
                        @Param("userNickname") String userNickname,
                        @Param("userPushYn") Boolean userPushYn,
                        @Param("userPushKey") String userPushKey,
                        @Param("authKey") String authKey
    ) throws SQLIntegrityConstraintViolationException;

    BaseResponse sendVerificationEmail(@Param("userEmail") String userEmail,
                                       @Param("emailAuthKind") EmailAuthKind emailAuthKind);

    User getUserInfoByUserEmail(@Param("userEmail") @Valid String userEmail);

    BaseResponse checkRedundancyNickname(@Param("userNickname") String userNickname);

    UserLoginDto changePassword(@Param("userEmail") String userEmail,
                                @Param("password") String password,
                                @Param("authKey") String authKey
    );
}
