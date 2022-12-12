package moska.rebora.Admin.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Schema(description = "페이징 DTO")
public class AdminTheaterResponseDto {

    @Schema(description = "성공 유뮤")
    private Boolean result; //반환 성공 유무
    private Page<AdminTheaterDto> page;
    private List<AdminRegionDto> adminRegionList;
}
