package moska.rebora.Comment.Conroller;

import io.swagger.v3.oas.annotations.tags.Tag;
import moska.rebora.Comment.Dto.CommentDto;
import moska.rebora.Comment.Service.CommentService;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@Tag(name = "댓글")
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 코멘트 리스트 가져오기
     *
     * @param pageable      페이징
     * @param recruitmentId 모집 아이디
     * @return BasePageResponse<CommentDto>
     */
    @Tag(name = "댓글")
    @GetMapping("/getCommentList/{recruitmentId}")
    public BasePageResponse<CommentDto> getCommentList(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                                       @PathVariable Long recruitmentId) {

        BasePageResponse<CommentDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(commentService.getCommentList(pageable, recruitmentId));

        return basePageResponse;
    }

    /**
     * 코멘트 생성
     *
     * @param recruitmentId  모집 아이디
     * @param commentContent 코멘트 내용
     * @return BaseResponse
     */
    @Tag(name = "댓글")
    @PostMapping("/createComment")
    public BaseResponse createComment(
            @RequestParam Long recruitmentId,
            @RequestParam String commentContent
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.createComment(recruitmentId, commentContent, userEmail);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        return baseResponse;
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글 아이디
     * @return BaseResponse
     */
    @Tag(name = "댓글")
    @DeleteMapping("/deleteComment/{commentId}")
    public BaseResponse deleteComment(
            @PathVariable Long commentId
    ) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);
        commentService.deleteComment(commentId, userEmail);

        return baseResponse;
    }
}
