package moska.rebora.Movie.Dto;

import lombok.Getter;
import lombok.Setter;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.User.DTO.UserMovieDto;
import moska.rebora.User.Entity.UserMovie;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MovieDto {

    private Long id;
    private String movieName;
    private MovieRating movieRating;
    private String movieCategory;
    private String movieDirector;
    private String movieImage;
    private String movieBannerImage;
    private String movieDetailLink;
    private Integer movieRunningTime;
    private Integer moviePopularCount;
    private Integer movieStarRating;
    private List<UserMovieDto> userMovieList;

    public MovieDto(Movie movie) {
        this.id = movie.getId();
        this.movieName = movie.getMovieName();
        this.movieRating = movie.getMovieRating();
        this.movieDirector = movie.getMovieDirector();
        this.movieImage = movie.getMovieImage();
        this.movieBannerImage = movie.getMovieBannerImage();
        this.movieDetailLink = movie.getMovieDetailLink();
        this.movieRunningTime = movie.getMovieRunningTime();
        this.moviePopularCount = movie.getMoviePopularCount();
        this.movieStarRating = movie.getMovieStarRating();
        this.userMovieList = movie.getUserMovieList().stream().map(UserMovieDto::new).collect(Collectors.toList());
    }
}
