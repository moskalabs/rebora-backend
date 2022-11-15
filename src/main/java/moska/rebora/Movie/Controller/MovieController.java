package moska.rebora.Movie.Controller;

import moska.rebora.Common.BasePageResponse;
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
public class MovieController {

    @Autowired
    MovieService movieService;

    /**
     * 영화 가져오기
     *
     * @param pageable 페이징
     * @param category 카테고리
     * @return BasePageResponse
     */
    @GetMapping("/getList")
    public BasePageResponse<MoviePageDto> getList(
            @PageableDefault(size = 9,page = 0, sort = "moviePopularCount", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "all") String category
    ) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setCategory(category);

        return movieService.getList(userSearchCondition, userEmail, pageable);
    }

    /**
     * 영화 검색 가져오기
     *
     * @param pageable 페이징
     * @param searchWord 검색어
     * @return BasePageResponse
     */
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
