package moska.rebora.Banner.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Recruitment.Entity.Recruitment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id", nullable = false)
    private Long id;

    @Column
    private Boolean bannerExposeYn;

    @Column
    private String bannerMainText;

    @Column
    private String bannerSubText;

    @Column
    private String bannerImage;

    @Column
    private String bannerUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    public void changeExpose(Boolean bannerExposeYn) {
        this.bannerExposeYn = bannerExposeYn;
    }

    public void deleteBanner(){
        this.recruitment = null;
    }

    @Builder
    public Banner(Boolean bannerExposeYn, String bannerMainText, String bannerSubText, String bannerImage, String bannerUrl, Recruitment recruitment) {
        this.bannerExposeYn = bannerExposeYn;
        this.bannerMainText = bannerMainText;
        this.bannerSubText = bannerSubText;
        this.bannerImage = bannerImage;
        this.bannerUrl = bannerUrl;
        this.recruitment = recruitment;
    }
}
