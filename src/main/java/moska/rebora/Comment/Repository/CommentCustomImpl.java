package moska.rebora.Comment.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Comment.Dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Comment.Entity.QComment.comment;
import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.Theater.Entity.QTheater.theater;
import static moska.rebora.User.Entity.QUser.user;

public class CommentCustomImpl implements CommentCustom {

    private final JPAQueryFactory queryFactory;

    public CommentCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<CommentDto> getCommentPage(Pageable pageable, Long recruitmentId) {

        List<CommentDto> content = queryFactory.select(Projections.fields(
                        CommentDto.class,
                        comment.id.as("commentId"),
                        comment.commentContent,
                        comment.regDate,
                        comment.modDate,
                        user.id.as("userId"),
                        user.userNickname,
                        user.userImage
                )).from(comment)
                .leftJoin(comment.user, user)
                .leftJoin(comment.recruitment, recruitment)
                .where(
                        recruitment.id.eq(recruitmentId),
                        comment.commentUseYn.eq(true)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(recruitment.id.count())
                .from(comment)
                .leftJoin(comment.user, user)
                .leftJoin(comment.recruitment, recruitment)
                .where(recruitment.id.eq(recruitmentId));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }
}
