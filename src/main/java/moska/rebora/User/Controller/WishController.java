package moska.rebora.User.Controller;

import moska.rebora.Common.BaseResponse;
import moska.rebora.User.Service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wish")
public class WishController {

    @Autowired
    WishService wishService;

    @PostMapping("/wishRecruitment")
    public BaseResponse wishRecruitment(
            @RequestParam(required = false) Long userRecruitmentId,
            @RequestParam Long recruitmentId,
            @RequestParam(required = false, defaultValue = "true") Boolean userRecruitmentWish
    ) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        wishService.wishRecruitment(userRecruitmentId, recruitmentId, SecurityContextHolder.getContext().getAuthentication().getName(), userRecruitmentWish);

        return baseResponse;
    }

    @PostMapping("/wishMovie")
    public BaseResponse wishMovie(
            @RequestParam(required = false) Long userMovieId,
            @RequestParam Long movieId,
            @RequestParam(required = false, defaultValue = "true") Boolean userMovieWish
    ) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(true);

        wishService.wishMovie(userMovieId, movieId, SecurityContextHolder.getContext().getAuthentication().getName(), userMovieWish);

        return baseResponse;
    }
}
