package moska.rebora.Theater.Controller;

import moska.rebora.Common.BaseInfoResponse;
import moska.rebora.Common.BaseListResponse;
import moska.rebora.Theater.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/theater")
public class TheaterController {

    @Autowired
    TheaterService theaterService;

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
}
