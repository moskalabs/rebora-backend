package moska.rebora.Movie.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Enum.MovieRating;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "영화 페이징 DTO")
public class MoviePageDto {

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
    @Schema(description = "영화 가격")
    private Integer moviePrice;
    @Schema(description = "영화 모집된 횟수")
    private Integer moviePopularCount;
    @Schema(description = "영화 평점")
    private Integer movieStarRating;
    @Schema(description = "영화 평점 변환")
    private String convertStartRation;
    private Boolean movieUseYn;
    @Schema(description = "유저_영화 아이디")
    private Long userMovieId;
    @Schema(description = "영화 찜 여부")
    private Boolean userMovieWish;
    @Schema(description = "영화 카테고리 리스트")
    private List<Category> categoryList;
}
