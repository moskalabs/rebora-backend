package moska.rebora.Recruitment.Repository;

import moska.rebora.Admin.Repository.AdminRecruitment;
import moska.rebora.Main.Repository.RecruitmentRepositoryMain;
import moska.rebora.Recruitment.Entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentRepositoryMain ,RecruitmentCustom, RecruitmentBatch, AdminRecruitment {

    Recruitment getRecruitmentById(Long id);


}
