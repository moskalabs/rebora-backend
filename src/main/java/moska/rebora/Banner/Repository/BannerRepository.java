package moska.rebora.Banner.Repository;

import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Recruitment.Entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom{
    Banner getBannerByRecruitment(Recruitment recruitment);

    Banner getBannerById(Long bannerId);
}
