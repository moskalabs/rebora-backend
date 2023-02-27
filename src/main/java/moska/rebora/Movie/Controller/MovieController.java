package moska.rebora.Movie.Controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Service.MovieService;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movie")
@Tag(name = "영화")
@AllArgsConstructor
@Slf4j
public class MovieController {
    MovieService movieService;

    /**
     * 영화 가져오기
     *
     * @param pageable 페이징
     * @param category 카테고리
     * @return BasePageResponse
     */
    @Tag(name = "영화")
    @Operation(summary = "영화 리스트 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "카테고리", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getList")
    public BasePageResponse<MoviePageDto> getList(
            @PageableDefault(size = 9, page = 0, sort = "moviePopularCount", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "all") String category
    ) {
        log.info("sort={} category={} size={}, page={}", pageable.getSort(), category, pageable.getPageSize(), pageable.getOffset());

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setCategory(category);

        return movieService.getList(userSearchCondition, userEmail, pageable);
    }

    /**
     * 영화 검색 가져오기
     *
     * @param pageable   페이징
     * @param searchWord 검색어
     * @return BasePageResponse
     */
    @Tag(name = "영화")
    @Operation(summary = "영화 리스트 검색으로 가져오기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchWord", value = "검색어", required = false)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = "오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/getSearchList")
    public BasePageResponse<MoviePageDto> getSearchList(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String searchWord
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setSearchWord(searchWord);

        return movieService.getList(userSearchCondition, userEmail, pageable);
    }
}
