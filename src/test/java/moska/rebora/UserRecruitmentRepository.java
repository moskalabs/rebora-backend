package moska.rebora;

import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRecruitmentRepository extends JpaRepository<UserRecruitment, Long> {
}
