package moska.rebora.Comment.Repository;

import moska.rebora.Comment.Dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentCustom {

    Page<CommentDto> getCommentPage(Pageable pageable, Long recruitmentId);
}
