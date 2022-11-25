package moska.rebora.Movie.Dto;

import lombok.Data;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Enum.MovieRating;

import java.util.List;

@Data
public class MoviePageDto{
    private Long id;
    private String movieName;
    private MovieRating movieRating;
    private String movieDirector;
    private String movieImage;
    private String movieBannerImage;
    private String movieDetailLink;
    private Integer movieRunningTime;
    private Integer moviePopularCount;
    private Integer movieStarRating;
    private String convertStartRation;
    private Long userMovieId;
    private Boolean userMovieWish;
    private List<Category> categoryList;
}
