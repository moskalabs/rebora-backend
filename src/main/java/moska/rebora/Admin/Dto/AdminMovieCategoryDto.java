package moska.rebora.Admin.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import moska.rebora.Common.Entity.Category;

@Data
public class AdminMovieCategoryDto {

    @Schema(description = "카테고리 아이디")
    private Long id;

    @Schema(description = "카테고리 이름")
    private String categoryName;

    @Schema(description = "카테고리 이름")
    private Boolean categoryYn;

    public AdminMovieCategoryDto(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.categoryYn = false;
    }
}
