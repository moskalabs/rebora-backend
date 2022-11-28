package moska.rebora.Theater.Controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import moska.rebora.Common.BaseListResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Theater.Dto.TheaterPageDto;
import moska.rebora.Theater.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theater")
@Tag(name = "상영관")
public class TheaterController {

    @Autowired
    TheaterService theaterService;

    @Tag(name = "상영관")
    @GetMapping("/getAvailableDate")
    public BaseListResponse<String> getAvailableDate(
            @RequestParam String theaterRegion,
            @RequestParam String selectMonth,
            @RequestParam Long movieId
    ) {
        BaseListResponse<String> baseListResponse = new BaseListResponse<>();
        baseListResponse.setResult(true);
        baseListResponse.setList(theaterService.getAvailableDate(theaterRegion, selectMonth, movieId));

        return baseListResponse;
    }

    @Tag(name = "상영관")
    @GetMapping("/getPageTheater")
    public BasePageResponse<TheaterPageDto> getPageTheater(
            @RequestParam String theaterRegion,
            @RequestParam String selectDate,
            @RequestParam Long movieId,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {

        BasePageResponse<TheaterPageDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(theaterService.getPageTheater(theaterRegion, selectDate, movieId, pageable));
        return basePageResponse;
    }
}
