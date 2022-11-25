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

    public void addMoviePopularCount(){
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

    @Builder
    public Movie(String movieName, MovieRating movieRating, String movieDirector, String movieImage, String movieBannerImage, String movieDetailLink, Integer movieRunningTime, Integer moviePopularCount, Integer movieStarRating, List<Recruitment> recruitmentList, List<BrandMovie> cinemaBrandMovieList,String movieRecruitmentImage) {
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
    }
}
