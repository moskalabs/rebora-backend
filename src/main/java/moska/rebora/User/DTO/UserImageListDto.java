package moska.rebora.User.DTO;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "유저 이미지 목록 DTO")
public class UserImageListDto {

    @Schema(description = "유저 이미지")
    private String userImage;
    @Schema(description = "유저 닉네임")
    private String userNickname;

    @QueryProjection
    public UserImageListDto(String userImage, String userNickname) {
        this.userImage = userImage;
        this.userNickname = userNickname;
    }

    public UserImageListDto(String userImage) {
        this.userImage = userImage;
    }
}
