package moska.rebora.Comment.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Comment.Dto.CommentDto;
import moska.rebora.Comment.Entity.Comment;
import moska.rebora.Comment.Repository.CommentRepository;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    UserRepository userRepository;
    RecruitmentRepository recruitmentRepository;
    NotificationService notificationService;

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
    @Transactional
    public void createComment(Long recruitmentId, String commentContent, String userEmail) {

        User user = userRepository.getUserByUserEmail(userEmail);
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findById(recruitmentId);


        if (user == null) {
            throw new NullPointerException("존재하지 않는 유저입니다 \n다시 시도해주세요");
        }

        if (optionalRecruitment.isEmpty()) {
            throw new NullPointerException("존재하지 않는 모집입니다 \n다시 시도해주세요");
        }

        Recruitment recruitment = optionalRecruitment.get();

        Movie movie = recruitment.getMovie();

        User recruiter = userRepository.getUserByUserEmail(recruitment.getCreatedBy());

        String subject = "모집한 게시물(" + movie.getMovieName() + ")에 댓글이 달렸습니다.";
        String content = movie.getMovieName() + "의 모집한 게시물에 댓글이 달렸습니다.";

        if (recruiter != null) {
            notificationService.createNotificationRecruitment(
                    subject, content, NotificationKind.RECRUITMENT, recruitment, "", recruiter
            );
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
