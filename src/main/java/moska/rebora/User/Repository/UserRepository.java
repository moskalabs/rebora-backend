package moska.rebora.User.Repository;

import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long> {

    User getUserById (Long id);

    User getUserByUserEmail(String userEmail);

    int countUSerByUserEmail(@Param("userEmail") String userEmail);

    int countUserByUserEmailOrUserNickname(@Param("userEmail") String userEmail, @Param("userNickname") String userNickname);
}
