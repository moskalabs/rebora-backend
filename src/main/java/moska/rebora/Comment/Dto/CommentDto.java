package moska.rebora.Comment.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "댓글 Dto")
public class CommentDto {
    @Schema(description = "댓글 아이디")
    private Long commentId;
    @Schema(description = "댓글 내용")
    private String commentContent;
    @Schema(description = "댓글 등록일")
    private LocalDateTime regDate;
    @Schema(description = "댓글 수정일")
    private LocalDateTime modDate;
    @Schema(description = "댓글 작성자 닉네임")
    private String userNickname;
    @Schema(description = "댓글 작성자 유저 아이디")
    private Long userId;
    @Schema(description = "댓글 작성자 이미지")
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
