package moska.rebora.User.Repository;

import moska.rebora.Admin.Repository.UserAdmin;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustom, UserAdmin {

    User getUserById(Long id);

    User getUserByUserEmail(String userEmail);

    Optional<User> getUserByUserSnsKindAndUserSnsId(UserSnsKind userSnsKind, String userSnsId);

    int countUSerByUserEmail(@Param("userEmail") String userEmail);

    int countUserByUserNickname(@Param("userNickname") String userNickname);

    int countUserByUserEmailOrUserNickname(@Param("userEmail") String userEmail, @Param("userNickname") String userNickname);
}
