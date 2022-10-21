package moska.rebora.Movie.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Cinema.Entity.BrandMovie;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Recruitment.Entity.Recruitment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "movie_id", nullable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String movie_name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MovieRating movie_rating;

    @Column(length = 50)
    private String movie_category;

    @Column(length = 20)
    private String movie_director;

    @Column
    private String movie_image;

    @Column
    private String movie_banner_image;

    @Column
    private String movie_detail_link;

    @Column
    private Integer movie_running_time;

    @Column
    private Integer movie_popular_count;

    @Column
    private Integer movie_star_rating;

    @OneToMany(mappedBy = "movie")
    List<Recruitment> recruitmentList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    List<BrandMovie> cinemaBrandMovieList = new ArrayList<>();

    @Builder
    public Movie(String movie_name, MovieRating movie_rating, String movie_category, String movie_director, String movie_image, String movie_banner_image, String movie_detail_link, Integer movie_running_time, Integer movie_popular_count, Integer movie_star_rating) {
        this.movie_name = movie_name;
        this.movie_rating = movie_rating;
        this.movie_category = movie_category;
        this.movie_director = movie_director;
        this.movie_image = movie_image;
        this.movie_banner_image = movie_banner_image;
        this.movie_detail_link = movie_detail_link;
        this.movie_running_time = movie_running_time;
        this.movie_popular_count = movie_popular_count;
        this.movie_star_rating = movie_star_rating;
    }
}
