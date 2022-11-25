package moska.rebora.Common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 기본 인증 정보
 */
@Getter
@Setter
@Schema(description = "기본 응답 DTO")
public class BaseResponse {

    @Schema(description = "반환 성공 여부")
    private Boolean result; //반환 성공 유무

    @Schema(description = "에러 코드")
    private String errorCode; //에러 코드

    @Schema(description = "메세지")
    private String message; // 메세지
}
