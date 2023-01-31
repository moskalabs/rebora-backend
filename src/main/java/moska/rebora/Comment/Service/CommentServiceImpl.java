package moska.rebora.Comment.Service;

import moska.rebora.Comment.Dto.CommentDto;
import moska.rebora.Comment.Entity.Comment;
import moska.rebora.Comment.Repository.CommentRepository;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    /**
     * 코멘트 리스트 가져오기
     *
     * @param pageable      페이징
     * @param recruitmentId 모집 아이디
     * @return Page<CommentDto>
     */
    @Override
    public Page<CommentDto> getCommentList(Pageable pageable, Long recruitmentId) {
        return commentRepository.getCommentPage(pageable, recruitmentId);
    }

    /**
     * 코멘트 생성
     *
     * @param recruitmentId  모집 아이디
     * @param commentContent 코멘트 내용
     * @param userEmail      유저 이메일
     */
    @Override
    public void createComment(Long recruitmentId, String commentContent, String userEmail) {

        User user = userRepository.getUserByUserEmail(userEmail);
        Recruitment recruitment = recruitmentRepository.getRecruitmentById(recruitmentId);

        if (user == null) {
            throw new NullPointerException("존재하지 않는 유저입니다 \n다시 시도해주세요");
        }

        if (recruitment == null) {
            throw new NullPointerException("존재하지 않는 모집입니다 \n다시 시도해주세요");
        }

        Comment comment = Comment.builder()
                .user(user)
                .recruitment(recruitment)
                .commentContent(commentContent)
                .build();

        commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId, String userEmail) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        User user = userRepository.getUserByUserEmail(userEmail);
        if (commentOptional.isEmpty()) {
            throw new NullPointerException("존재하지 않는 댓글입니다.");
        }

        Comment comment = commentOptional.get();
        User commentUser = comment.getUser();

        if (!Objects.equals(user.getId(), commentUser.getId())) {
            if (user.getUserGrade().equals(UserGrade.ADMIN)) {
                comment.deleteComment();
                commentRepository.save(comment);
            } else {
                throw new RuntimeException("관리자 및 본인만 삭제 가능합니다.");
            }
        } else {
            comment.deleteComment();
            commentRepository.save(comment);
        }
    }
}
