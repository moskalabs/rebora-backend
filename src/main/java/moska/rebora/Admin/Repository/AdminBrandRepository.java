package moska.rebora.Admin.Repository;

import moska.rebora.Admin.Dto.AdminBrandDto;
import moska.rebora.Cinema.Entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminBrandRepository {

    Page<AdminBrandDto> getPageBrandList(Pageable pageable);
}
