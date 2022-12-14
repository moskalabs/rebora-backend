package moska.rebora.Banner.Service;

import moska.rebora.Banner.Entity.Banner;
import org.springframework.data.repository.query.Param;

public interface BannerService {

    void bannerDelete(@Param("banner") Banner banner);
}
