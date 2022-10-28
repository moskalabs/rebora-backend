package moska.rebora.User.Service;

import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.Entity.UserEmailAuth;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;

public interface UserEmailAuthService {

    public UserEmailAuth checkUserEmailAuth(String userEmail, String authKey, EmailAuthKind emailAuthKind);

    public void sendSignUpEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber);

    public void sendPasswordEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber);

    public JSONObject validationEmailCode(@Param("userEmail") String userEmail,
                                          @Param("verifyNumber") String verifyNumber,
                                          @Param("emailAuthKind") EmailAuthKind emailAuthKind

    );
}
