package moska.rebora.Admin.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Admin.Dto.AdminBrandDto;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Cinema.Repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminBrandServiceImpl implements AdminBrandService {

    BrandRepository brandRepository;

    @Override
    public Page<AdminBrandDto> getBrandList(Pageable pageable) {
        return brandRepository.getPageBrandList(pageable);
    }

    @Override
    public void saveBrand(Long brandId, String brandName) {

        if (brandId != null) {
            Optional<Brand> brandOptional = brandRepository.findById(brandId);

            if (brandOptional.isEmpty()) {
                throw new NullPointerException("존재하지 않는 브랜드입니다.");
            }

            Brand brand = brandOptional.get();
            brand.changeBrandName(brandName);
            brandRepository.save(brand);

        } else {
            Brand brand = Brand.builder()
                    .brandName(brandName)
                    .build();

            brandRepository.save(brand);
        }
    }

    @Override
    public void deleteBrand(Long brandId) {

        Optional<Brand> brandOptional = brandRepository.findById(brandId);

        if (brandOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 브랜드입니다.");
        }

        Brand brand = brandOptional.get();
        brandRepository.delete(brand);
    }
}
