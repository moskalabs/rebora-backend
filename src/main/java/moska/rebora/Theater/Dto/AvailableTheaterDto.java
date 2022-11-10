package moska.rebora.Theater.Dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AvailableTheaterDto {

    List<String> availableRegion;
    List<String> availableYear;
    List<String> availableMonth;
    List<String> availableDate;
    private LocalDateTime theaterStartDatetime;
}
