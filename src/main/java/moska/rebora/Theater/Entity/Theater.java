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
    private String theater_name;

    @Column
    private LocalDateTime theater_start_datetime;

    @Column
    private LocalDateTime theater_end_datetime;

    @Column(length = 10)
    private String theater_day;

    @Column
    private Integer theater_people;

    @Column
    private Integer theater_min_people;

    @Column(length = 50)
    private String theater_cinema_name;

    @Column(length = 50)
    private String theater_cinema_brand_name;

    @Column(length = 50)
    private String theater_region;

    @Column
    private Integer theater_time;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitment_id")
    private Recruitment recruitment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Builder
    public Theater(String theater_name, LocalDateTime theater_start_datetime, LocalDateTime theater_end_datetime, String theater_day, Integer theater_people, Integer theater_min_people, String theater_cinema_name, String theater_cinema_brand_name, String theater_region, Integer theater_time, Recruitment recruitment, Brand brand) {
        this.theater_name = theater_name;
        this.theater_start_datetime = theater_start_datetime;
        this.theater_end_datetime = theater_end_datetime;
        this.theater_day = theater_day;
        this.theater_people = theater_people;
        this.theater_min_people = theater_min_people;
        this.theater_cinema_name = theater_cinema_name;
        this.theater_cinema_brand_name = theater_cinema_brand_name;
        this.theater_region = theater_region;
        this.theater_time = theater_time;
        this.recruitment = recruitment;
        this.brand = brand;
    }
}
