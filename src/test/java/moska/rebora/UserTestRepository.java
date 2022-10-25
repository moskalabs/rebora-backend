package moska.rebora;

import moska.rebora.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTestRepository extends JpaRepository<User, Long> {
}
