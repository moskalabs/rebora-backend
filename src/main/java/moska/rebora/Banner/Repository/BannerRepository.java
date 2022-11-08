package moska.rebora.Banner.Repository;

import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.Banner.Entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long>, BannerRepositoryCustom{

}
