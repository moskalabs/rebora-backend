package moska.rebora.Comment.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(length = 1000,nullable = false)
    private String commentContent;

    @Column
    private Boolean commentUseYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    public void deleteComment(){
        commentUseYn = false;
    }

    @Builder
    public Comment(String commentContent, User user, Recruitment recruitment) {
        this.commentContent = commentContent;
        this.user = user;
        this.recruitment = recruitment;
        this.commentUseYn = true;
    }
}
