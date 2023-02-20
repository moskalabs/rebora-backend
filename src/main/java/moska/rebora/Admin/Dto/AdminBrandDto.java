package moska.rebora.Admin.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Cinema.Entity.Brand;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdminBrandDto {
    private Long brandId;
    private String brandName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    public AdminBrandDto(Brand brand) {
        this.brandId = brand.getId();
        this.brandName = brand.getBrandName();
        this.regDate = brand.getRegDate();
        this.modDate = brand.getModDate();
    }
}
