package moska.rebora.Movie.Dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.MovieRating;
import moska.rebora.User.DTO.UserMovieDto;

import java.util.List;

@Data
public class MoviePageDto{
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
    private Boolean userMovieWish;
}
