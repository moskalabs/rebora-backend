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
    private String recruitmentIntroduce;

    @Column
    private String recruitmentMainText;

    @Column
    private String recruitmentSubText;

    @Column(nullable = false)
    private LocalDateTime recruitmentEndDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStatus recruitmentStatus;

    @Column(nullable = false)
    private Boolean recruitmentBanner;

    @Column(nullable = false)
    private Boolean recruitmentExposeYn;

    @OneToMany(mappedBy = "recruitment")
    List<UserRecruitment> userRecruitmentList = new ArrayList<UserRecruitment>();

    @OneToOne(mappedBy = "recruitment", fetch = FetchType.LAZY)
    private Theater theater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public Recruitment(String recruitmentIntroduce, String recruitmentMainText, String recruitmentSubText, LocalDateTime recruitmentEndDate, RecruitmentStatus recruitmentStatus, Boolean recruitmentBanner, Boolean recruitmentExposeYn, Theater theater, Movie movie) {
        this.recruitmentIntroduce = recruitmentIntroduce;
        this.recruitmentMainText = recruitmentMainText;
        this.recruitmentSubText = recruitmentSubText;
        this.recruitmentEndDate = recruitmentEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.recruitmentBanner = recruitmentBanner;
        this.recruitmentExposeYn = recruitmentExposeYn;
        this.theater = theater;
        this.movie = movie;
    }
}
