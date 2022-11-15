package moska.rebora.User.Repository;

import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRecruitmentRepository extends JpaRepository<UserRecruitment, Long> , UserRecruitmentCustom {

    Optional<UserRecruitment> getUserRecruitmentByUserAndRecruitment(@Param("user") User user, @Param("recruitment") Recruitment recruitment);
}
