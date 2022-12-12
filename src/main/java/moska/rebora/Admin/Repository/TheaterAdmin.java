package moska.rebora.Admin.Repository;

import moska.rebora.Admin.Dto.AdminRegionDto;
import moska.rebora.Admin.Dto.AdminTheaterDto;
import moska.rebora.Theater.Dto.TheaterPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;

public interface TheaterAdmin {

    Page<AdminTheaterDto> getAdminPage(String theaterRegion, String theaterCinemaBrandName, LocalDate selectDate, Pageable pageable);

    List<AdminRegionDto> getAdminRegion();

    AdminTheaterDto getAdminDetail(Long theaterId);
}
