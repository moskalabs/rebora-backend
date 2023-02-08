package moska.rebora.Movie.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Cinema.Entity.BrandMovie;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.Entity.UserMovie;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {
})
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String movieName;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MovieRating movieRating;

    @Column(length = 20)
    private String movieDirector;

    @Column
    private String movieImage;

    @Column
    private String movieBannerImage;

    @Column
    private String movieRecruitmentImage;

    @Column
    private String movieDetailLink;

    @Column
    private Integer movieRunningTime;

    @Column
    private Integer moviePopularCount;

    @Column
    private Integer movieStarRating;

    @Column(columnDefinition = "boolean default false")
    private Boolean movieUseYn;

    @Column(columnDefinition = "integer default 0")
    private Integer moviePrice;

    public void addMoviePopularCount() {
        this.moviePopularCount += 1;
    }

    @OneToMany(mappedBy = "movie")
    List<Recruitment> recruitmentList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    List<UserMovie> userMovieList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    List<MovieCategory> movieCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    List<BrandMovie> cinemaBrandMovieList = new ArrayList<>();

    public void changeMoviePopularCount(int moviePopularCount) {
        this.moviePopularCount = moviePopularCount / 10;
    }

    public void changeMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }

    public void changeMovieBannerImage(String movieBannerImage) {
        this.movieBannerImage = movieBannerImage;
    }

    public void changeMovieRecruitmentImage(String movieRecruitmentImage) {
        this.movieRecruitmentImage = movieRecruitmentImage;
    }

    public void updateMovie(
            String movieName,
            String movieRating,
            String movieDirector,
            Integer movieStarRating,
            String movieDetailLink,
            Integer moviePopularCount,
            Integer movieRunningTime,
            Integer moviePrice,
            Boolean movieUseYn
    ) {
        this.movieName = movieName;
        this.movieRating = MovieRating.valueOf(movieRating);
        this.movieDirector = movieDirector;
        this.movieStarRating = movieStarRating;
        this.movieDetailLink = movieDetailLink;
        this.moviePopularCount = moviePopularCount;
        this.movieRunningTime = movieRunningTime;
        this.moviePrice = moviePrice;
        this.movieUseYn = movieUseYn;
    }

    public void addImage(
            String movieImage,
            String movieBannerImage,
            String movieRecruitmentImage
    ) {
        this.movieImage = movieImage;
        this.movieBannerImage = movieBannerImage;
        this.movieRecruitmentImage = movieRecruitmentImage;
        this.movieUseYn = true;
    }

    @Builder
    public Movie(String movieName, MovieRating movieRating, String movieDirector, String movieImage, String movieBannerImage, String movieDetailLink, Integer movieRunningTime, Integer moviePopularCount, Integer movieStarRating, List<Recruitment> recruitmentList, List<BrandMovie> cinemaBrandMovieList, String movieRecruitmentImage, Boolean movieUseYn, Integer moviePrice) {
        this.movieName = movieName;
        this.movieRating = movieRating;
        this.movieDirector = movieDirector;
        this.movieImage = movieImage;
        this.movieBannerImage = movieBannerImage;
        this.movieDetailLink = movieDetailLink;
        this.movieRunningTime = movieRunningTime;
        this.moviePopularCount = moviePopularCount;
        this.movieStarRating = movieStarRating;
        this.recruitmentList = recruitmentList;
        this.cinemaBrandMovieList = cinemaBrandMovieList;
        this.movieRecruitmentImage = movieRecruitmentImage;
        this.movieUseYn = movieUseYn;
        this.moviePrice = moviePrice;
    }
}
