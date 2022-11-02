package moska.rebora.User.DTO;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserImageListDto {

    private String userImage;

    @QueryProjection
    public UserImageListDto(String userImage) {
        this.userImage = userImage;
    }
}
