package moska.rebora.Banner.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import moska.rebora.User.DTO.UserDto;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "메인 화면 DTO")
public class BannerDto {

    @Schema(description = "배너 아이디")
    private Long banner_id;
    @Schema(description = "배너 노출 여부")
    private Boolean bannerExposeYn;
    @Schema(description = "배너 메인 텍스트")
    private String bannerMainText;
    @Schema(description = "배너 서브 텍스트")
    private String bannerSubText;
    @Schema(description = "배너 이미지")
    private String bannerImage;
    @Schema(description = "배너 Url")
    private String bannerUrl;
    @Schema(description = "유저 이미지")
    private String userImage;
    @Schema(description = "유저 닉네임")
    private String userNickname;
    @Schema(description = "모집 아이디")
    private Long recruitment_id;
    @Schema(description = "영화 배너 이미지")
    private String movieBannerImage;

    public BannerDto(Boolean bannerExposeYn, String bannerMainText, String bannerSubText, String bannerImage, String bannerUrl, String userImage, String userNickname, Long recruitment_id, String movieBannerImage) {
        this.bannerExposeYn = bannerExposeYn;
        this.bannerMainText = bannerMainText;
        this.bannerSubText = bannerSubText;
        this.bannerImage = bannerImage;
        this.bannerUrl = bannerUrl;
        this.userImage = userImage;
        this.userNickname = userNickname;
        this.recruitment_id = recruitment_id;
        this.movieBannerImage = movieBannerImage;
    }
}
