package moska.rebora.Recruitment.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.Entity.UserRecruitment;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id", nullable = false)
    private Long id;

    @Column
    private String recruitment_introduce;

    @Column
    private String recruitment_main_text;

    @Column
    private String recruitment_sub_text;

    @Column(nullable = false)
    private LocalDateTime recruitment_end_date;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStatus recruitment_status;

    @Column(nullable = false)
    private Boolean recruitment_banner;

    @Column(nullable = false)
    private Boolean recruitment_expose_yn;

    @OneToMany(mappedBy = "recruitment")
    List<UserRecruitment> userRecruitmentList = new ArrayList<UserRecruitment>();

    @OneToOne(mappedBy = "recruitment", fetch = FetchType.LAZY)
    private Theater theater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public Recruitment(String recruitment_introduce, String recruitment_main_text, String recruitment_sub_text, LocalDateTime recruitment_end_date, RecruitmentStatus recruitment_status, Boolean recruitment_banner, Boolean recruitment_expose_yn, Theater theater, Movie movie) {
        this.recruitment_introduce = recruitment_introduce;
        this.recruitment_main_text = recruitment_main_text;
        this.recruitment_sub_text = recruitment_sub_text;
        this.recruitment_end_date = recruitment_end_date;
        this.recruitment_status = recruitment_status;
        this.recruitment_banner = recruitment_banner;
        this.recruitment_expose_yn = recruitment_expose_yn;
        this.theater = theater;
        this.movie = movie;
    }
}
