package moska.rebora.Admin.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Enum.MovieRating;
import moska.rebora.Movie.Dto.MoviePageDto;

import java.util.List;

@Data
public class AdminMovieDto {

    @Schema(description = "영화 아이디")
    private Long id;
    @Schema(description = "영화 이름")
    private String movieName;
    @Schema(description = "영화 관람 등급")
    private MovieRating movieRating;
    @Schema(description = "영화 감독")
    private String movieDirector;
    @Schema(description = "영화 포스터 이미지 경로")
    private String movieImage;
    @Schema(description = "영화 배너 이미지 경로")
    private String movieBannerImage;
    @Schema(description = "영화 모집 이미지")
    private String movieRecruitmentImage;
    @Schema(description = "영화 상세 경로")
    private String movieDetailLink;
    @Schema(description = "영화 상영시간")
    private Integer movieRunningTime;
    @Schema(description = "영화 모집된 횟수")
    private Integer moviePopularCount;
    @Schema(description = "영화 평점 변환")
    private String convertStartRation;

    @Schema(description = "영화 카테고리 리스트")
    private List<AdminMovieCategoryDto> categoryList;

    public AdminMovieDto(MoviePageDto moviePageDto) {
        this.id = moviePageDto.getId();
        this.movieName = moviePageDto.getMovieName();
        this.movieRating = moviePageDto.getMovieRating();
        this.movieDirector = moviePageDto.getMovieDirector();
        this.movieImage = moviePageDto.getMovieImage();
        this.movieBannerImage = moviePageDto.getMovieBannerImage();
        this.movieRecruitmentImage = moviePageDto.getMovieRecruitmentImage();
        this.movieDetailLink = moviePageDto.getMovieDetailLink();
        this.movieRunningTime = moviePageDto.getMovieRunningTime();
        this.moviePopularCount = moviePageDto.getMoviePopularCount();
        this.convertStartRation = moviePageDto.getConvertStartRation();
    }

    public AdminMovieDto(){

    }
}
