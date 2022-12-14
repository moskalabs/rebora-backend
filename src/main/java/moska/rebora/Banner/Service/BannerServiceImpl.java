package moska.rebora.Banner.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {

    BannerRepository bannerRepository;
    MainBannerRepository mainBannerRepository;

    @Override
    public void bannerDelete(Banner banner) {
        if (banner != null) {
            MainBanner mainBanner = mainBannerRepository.getMainBannerByBanner(banner);
            if (mainBanner != null) {
                mainBannerRepository.delete(mainBanner);
            }
            bannerRepository.delete(banner);
        }
    }
}
