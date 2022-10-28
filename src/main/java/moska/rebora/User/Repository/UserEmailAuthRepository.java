package moska.rebora.User.Repository;

import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.Entity.UserEmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

public interface UserEmailAuthRepository extends JpaRepository<UserEmailAuth, Long> {

    Optional<UserEmailAuth> getUserEmailAuthByEmailAndExpireYnAndEmailAuthKind(@Param("email") String email,
                                                                              Boolean expireYn,
                                                                              EmailAuthKind emailAuthKind
    );
}
