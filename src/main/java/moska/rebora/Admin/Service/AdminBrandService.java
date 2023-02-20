package moska.rebora.Admin.Service;

import moska.rebora.Admin.Dto.AdminBrandDto;
import moska.rebora.Cinema.Entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminBrandService {

    Page<AdminBrandDto> getBrandList(Pageable pageable);

    void saveBrand(Long brandId, String brandName);

    void deleteBrand(Long brandId);
}
