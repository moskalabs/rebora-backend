package moska.rebora.Comment.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDto {

    private Long commentId;
    private String commentContent;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String userNickname;
    private Long userId;
    private String userImage;

    public CommentDto(Long commentId, String commentContent, LocalDateTime regDate, LocalDateTime modDate, String userNickname, Long userId, String userImage) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.regDate = regDate;
        this.modDate = modDate;
        this.userNickname = userNickname;
        this.userId = userId;
        this.userImage = userImage;
    }
}
