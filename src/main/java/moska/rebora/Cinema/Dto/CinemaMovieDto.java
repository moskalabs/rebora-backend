package moska.rebora.Cinema.Dto;

import lombok.Data;
import moska.rebora.Cinema.Entity.Cinema;

@Data
public class CinemaMovieDto {

    private Long cinemaId;
    private String cinemaName;
    private String brandName;
    private String regionName;

    private Boolean cinemaYn;

    public CinemaMovieDto() {
    }

    public CinemaMovieDto(Cinema cinema) {
        this.cinemaId = cinema.getId();
        this.cinemaName = cinema.getCinemaName();
        this.brandName = cinema.getBrandName();
        this.regionName = cinema.getRegionName();
        this.cinemaYn = false;
    }
}
