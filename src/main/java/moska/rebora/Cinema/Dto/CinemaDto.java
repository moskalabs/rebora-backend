package moska.rebora.Cinema.Dto;

import lombok.Data;
import moska.rebora.Cinema.Entity.Cinema;

@Data
public class CinemaDto {

    private Long id;
    private String brandName;
    private String regionName;
    private String cinemaName;

    public CinemaDto(Cinema cinema) {
        this.id = cinema.getId();
        this.brandName = cinema.getBrandName();
        this.regionName = cinema.getRegionName();
        this.cinemaName = cinema.getCinemaName();
    }
}
