package moska.rebora.Cinema.Repository;

import moska.rebora.Admin.Repository.AdminBrandRepository;
import moska.rebora.Cinema.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long>, AdminBrandRepository {

    Brand getBrandByBrandName(String brandName);
}
