package moska.rebora.Banner.Repository;

import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainBannerRepository extends JpaRepository<MainBanner, Long>, MainBannerCustom {

    MainBanner getMainBannerByBanner(Banner banner);
}
