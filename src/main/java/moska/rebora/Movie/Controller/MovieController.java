package moska.rebora.Movie.Controller;

import moska.rebora.Common.BasePageResponse;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Service.MovieService;
import moska.rebora.User.DTO.UserSearchCondition;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/getList")
    public BasePageResponse<MoviePageDto> getList(
            @RequestParam(defaultValue = "popular") String orderByMovie,
            @RequestParam(defaultValue = "all") String category
    ){

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(0, 9, Sort.by("moviePopularCount").descending());
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setOrderByMovie(orderByMovie);
        userSearchCondition.setCategory(category);

        return movieService.getList(userSearchCondition, userEmail, pageable);
    }
}
