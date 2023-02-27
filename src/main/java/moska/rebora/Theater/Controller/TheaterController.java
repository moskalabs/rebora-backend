package moska.rebora.Theater.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import moska.rebora.Common.BaseListResponse;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
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
@AllArgsConstructor
public class TheaterController {
    TheaterService theaterService;

    @Tag(name = "상영관")
    @Operation(summary = "모집 가능 지역")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "movieId", value = "영화 ID", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getAvailableRegion")
    public BaseListResponse<String> getAvailableRegion(@RequestParam Long movieId) {
        BaseListResponse<String> baseListResponse = new BaseListResponse<>();
        baseListResponse.setResult(true);
        baseListResponse.setList(theaterService.getAvailableDateRegion(movieId));

        return baseListResponse;
    }

    @Tag(name = "상영관")
    @Operation(summary = "모집 가능 날짜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "theaterRegion", value = "상영 지역", required = true),
            @ApiImplicitParam(name = "selectMonth", value = "선택 월", required = true),
            @ApiImplicitParam(name = "movieId", value = "영화 ID", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getAvailableDate")
    public BaseListResponse<String> getAvailableDate(
            @RequestParam String theaterRegion,
            @RequestParam String selectMonth,
            @RequestParam Long movieId) {
        BaseListResponse<String> baseListResponse = new BaseListResponse<>();
        baseListResponse.setResult(true);
        baseListResponse.setList(theaterService.getAvailableDate(theaterRegion, selectMonth, movieId));

        return baseListResponse;
    }

    @Tag(name = "상영관")
    @Operation(summary = "상영 가능 상영관")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "theaterRegion", value = "상영 지역", required = true),
            @ApiImplicitParam(name = "selectDate", value = "선택 날짜", required = true),
            @ApiImplicitParam(name = "movieId", value = "영화 ID", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "인증 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getPageTheater")
    public BasePageResponse<TheaterPageDto> getPageTheater(
            @RequestParam String theaterRegion,
            @RequestParam String selectDate,
            @RequestParam Long movieId,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        BasePageResponse<TheaterPageDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(theaterService.getPageTheater(theaterRegion, selectDate, movieId, pageable));
        return basePageResponse;
    }
}
