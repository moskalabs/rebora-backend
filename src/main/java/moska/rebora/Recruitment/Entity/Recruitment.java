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

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
        @Index(name = "i_reg_date", columnList = "reg_date")
        , @Index(name = "recruitmentGetList", columnList = "recruitmentStatus, recruitmentExposeYn, recruitmentEndDate")
})
public class Recruitment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruitment_id", nullable = false)
    private Long id;

    @Column
    private String recruitmentIntroduce;

    @Column(nullable = false)
    private LocalDateTime recruitmentEndDate;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private RecruitmentStatus recruitmentStatus;

    @Column
    private Integer recruitmentPeople;

    @Column(nullable = false)
    private Boolean recruitmentExposeYn;

    @OneToMany(mappedBy = "recruitment")
    List<UserRecruitment> userRecruitmentList = new ArrayList<UserRecruitment>();

    @OneToOne(mappedBy = "recruitment", fetch = FetchType.LAZY)
    private Theater theater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public void updateMovie(Movie movie) {
        this.movie = movie;
    }

    public void plusRecruitmentPeople(Integer recruitmentPeople) {
        this.recruitmentPeople += recruitmentPeople;
    }

    public void minusRecruitmentPeople(Integer recruitmentPeople) {
        this.recruitmentPeople = recruitmentPeople;
    }

    public void updateRecruitmentPeople(Integer recruitmentPeople) {
        this.recruitmentPeople = recruitmentPeople;
    }

    public void updateRecruitmentStatus(RecruitmentStatus recruitmentStatus) {
        this.recruitmentStatus = recruitmentStatus;
    }

    public void changeExpose(Boolean recruitmentExposeYn) {
        this.recruitmentExposeYn = recruitmentExposeYn;
    }

    @Builder
    public Recruitment(String recruitmentIntroduce, LocalDateTime recruitmentEndDate, RecruitmentStatus recruitmentStatus, Boolean recruitmentExposeYn, Theater theater, Movie movie, Integer recruitmentPeople) {

        this.recruitmentIntroduce = recruitmentIntroduce;
        this.recruitmentEndDate = recruitmentEndDate;
        this.recruitmentStatus = recruitmentStatus;
        this.recruitmentExposeYn = recruitmentExposeYn;
        this.recruitmentPeople = recruitmentPeople;
        this.theater = theater;
        this.movie = movie;
    }
}
