package moska.rebora.User.Service;

import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.DTO.UserEmailDto;
import moska.rebora.User.Entity.UserEmailAuth;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

public interface UserEmailAuthService {

    UserEmailAuth checkUserEmailAuth(String userEmail, String authKey, EmailAuthKind emailAuthKind);

    void sendSignUpEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber);

    void sendPasswordEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber);

    UserEmailDto validationEmailCode(@Param("userEmail") String userEmail,
                                     @Param("verifyNumber") String verifyNumber,
                                     @Param("emailAuthKind") EmailAuthKind emailAuthKind

    );
}
