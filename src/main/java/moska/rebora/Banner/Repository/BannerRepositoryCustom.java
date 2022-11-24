package moska.rebora.Banner.Repository;

import moska.rebora.Banner.Dto.BannerCompareDto;
import moska.rebora.Banner.Dto.BannerDto;

import java.util.List;

public interface BannerRepositoryCustom {

    List<BannerDto> getBannerRecently();

    List<BannerDto> getBannerUnderCapacity();

    List<BannerCompareDto> getCompareBannerList();
}
