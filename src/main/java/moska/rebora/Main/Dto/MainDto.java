package moska.rebora.Main.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "메인 화면 DTO")
public class MainDto extends BaseResponse {

    @Schema(description = "모집 리스트")
    private List<UserRecruitmentListDto> recruitmentList;

    @Schema(description = "영화 리스트")
    private List<MoviePageDto> movieList;

    @Schema(description = "배너 리스트")
    List<BannerDto> bannerList;
}
