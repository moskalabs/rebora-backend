package moska.rebora.User.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public
class MypageUpdateDto{

    private String userNickname;
    private String currentPassword;
    private String changePassword;
    private MultipartFile file;
}
