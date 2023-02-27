package moska.rebora.Comment.Conroller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CommentController {
    CommentService commentService;

    /**
     * 코멘트 리스트 가져오기
     *
     * @param pageable      페이징
     * @param recruitmentId 모집 아이디
     * @return BasePageResponse<CommentDto>
     */
    @Tag(name = "댓글")
    @Operation(summary = "댓글 리스트 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 리스트 가져오기 성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getCommentList/{recruitmentId}")
    public BasePageResponse<CommentDto> getCommentList(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long recruitmentId
    ) {

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
    @Operation(summary = "댓글 작성")
    @PostMapping("/createComment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true),
            @ApiImplicitParam(name = "commentContent", value = "모집 댓글 내용", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
    @Operation(summary = "댓글 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recruitmentId", value = "모집 아이디", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 완료"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
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
