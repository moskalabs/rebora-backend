package moska.rebora.User.Repository;

import moska.rebora.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserById (Long id);

    User getUserByUserEmail(String user_email);

    int countUserByUserEmailOrUserNickname(@Param("userEmail") String userEmail, @Param("userNickname") String userNickname);
}
