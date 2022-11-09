package moska.rebora.Comment.Conroller;

import moska.rebora.Comment.Dto.CommentDto;
import moska.rebora.Comment.Repository.CommentRepository;
import moska.rebora.Comment.Service.CommentService;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
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
    @PostMapping("/createComment/{recruitmentId}")
    public BaseResponse createComment(
            @PathVariable Long recruitmentId,
            @RequestParam String commentContent
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        commentService.createComment(recruitmentId, commentContent, userEmail);

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        return baseResponse;
    }
}
