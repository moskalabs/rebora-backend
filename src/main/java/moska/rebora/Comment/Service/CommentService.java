package moska.rebora.Comment.Service;

import moska.rebora.Comment.Dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface CommentService {

    Page<CommentDto> getCommentList(@Param("pageable") Pageable pageable,@Param("recruitmentId") Long recruitmentId);

    void createComment(@Param("recruitmentId") Long recruitmentId,
                       @Param("commentContent") String commentContent,
                       @Param("userEmail") String userEmail
                       );
}
