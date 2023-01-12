package moska.rebora.Theater.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Common.BaseEntity;
import moska.rebora.Recruitment.Entity.Recruitment;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Theater extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theater_id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String theaterName;

    @Column
    private LocalDateTime theaterStartDatetime;

    @Column
    private LocalDateTime theaterEndDatetime;

    @Column(length = 10)
    private String theaterDay;

    @Column
    private Integer theaterMaxPeople;

    @Column
    private Integer theaterMinPeople;

    @Column(length = 50)
    private String theaterCinemaName;

    @Column(length = 50)
    private String theaterCinemaBrandName;

    @Column(length = 50)
    private String theaterRegion;

    @Column
    private Integer theaterPrice;

    @Column
    private Integer theaterTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id", unique = true)
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public void addRecruitment(Recruitment recruitment){
        this.recruitment = recruitment;
    }

    public void updateTheater(String theaterName, LocalDateTime theaterStartDatetime, LocalDateTime theaterEndDatetime, String theaterDay, Integer theaterMaxPeople, Integer theaterMinPeople, String theaterCinemaName, String theaterCinemaBrandName, String theaterRegion, Integer theaterPrice, Integer theaterTime, Brand brand) {
        this.theaterName = theaterName;
        this.theaterStartDatetime = theaterStartDatetime;
        this.theaterEndDatetime = theaterEndDatetime;
        this.theaterDay = theaterDay;
        this.theaterMaxPeople = theaterMaxPeople;
        this.theaterMinPeople = theaterMinPeople;
        this.theaterCinemaName = theaterCinemaName;
        this.theaterCinemaBrandName = theaterCinemaBrandName;
        this.theaterRegion = theaterRegion;
        this.theaterPrice = theaterPrice;
        this.theaterTime = theaterTime;
        this.brand = brand;
    }

    @Builder
    public Theater(String theaterName, LocalDateTime theaterStartDatetime, LocalDateTime theaterEndDatetime, String theaterDay, Integer theaterMaxPeople, Integer theaterMinPeople, String theaterCinemaName, String theaterCinemaBrandName, String theaterRegion, Integer theaterPrice, Integer theaterTime, Recruitment recruitment, Brand brand) {
        this.theaterName = theaterName;
        this.theaterStartDatetime = theaterStartDatetime;
        this.theaterEndDatetime = theaterEndDatetime;
        this.theaterDay = theaterDay;
        this.theaterMaxPeople = theaterMaxPeople;
        this.theaterMinPeople = theaterMinPeople;
        this.theaterCinemaName = theaterCinemaName;
        this.theaterCinemaBrandName = theaterCinemaBrandName;
        this.theaterRegion = theaterRegion;
        this.theaterPrice = theaterPrice;
        this.theaterTime = theaterTime;
        this.recruitment = recruitment;
        this.brand = brand;
    }
}
