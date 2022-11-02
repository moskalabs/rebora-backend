package moska.rebora.Recruitment.Repository;

import moska.rebora.Recruitment.Entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    Recruitment getRecruitmentById(Long id);
}
