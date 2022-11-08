package moska.rebora.Banner.Dto;

import lombok.*;
import moska.rebora.User.DTO.UserDto;

@Getter
@Setter
@NoArgsConstructor
public class BannerDto {

    private Long banner_id;
    private Boolean bannerExposeYn;
    private String bannerMainText;
    private String bannerSubText;
    private String bannerImage;
    private String bannerUrl;
    private String userImage;
    private String userNickname;
    private Long recruitment_id;
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
