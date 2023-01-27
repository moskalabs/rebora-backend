package moska.rebora.Cinema.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.Entity.UserRecruitment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id", nullable = false)
    private Long id;

    @Column(length = 50, nullable = false)
    private String cinemaName;

    @Column(length = 50, nullable = false)
    private String brandName;

    @Column(length = 50, nullable = false)
    private String regionName;

    @Column(columnDefinition = "boolean default true")
    private Boolean cinemaUseYn;

    @OneToMany(mappedBy = "cinema")
    List<Theater> theaterList = new ArrayList<Theater>();

    public void updateBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void updateRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void updateCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    @Builder
    public Cinema(Long id, String cinemaName, List<Theater> cinemaList, String brandName, String regionName, Boolean cinemaUseYn) {
        this.id = id;
        this.cinemaName = cinemaName;
        this.theaterList = cinemaList;
        this.brandName = brandName;
        this.regionName = regionName;
        this.cinemaUseYn = cinemaUseYn;
    }
}
