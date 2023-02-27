package moska.rebora.Recruitment.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "모집 생성 Dto")
public class CreateRecruitmentDto {
    @Schema(description = "모집 Id")
    private Long recruitmentId;
}
