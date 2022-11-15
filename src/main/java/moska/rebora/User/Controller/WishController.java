package moska.rebora.User.Controller;

import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.Service.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wish")
public class WishController {

    @Autowired
    WishService wishService;

    /**
     * 모집 찜 하기
     *
     * @param userRecruitmentId   유저모집 PK
     * @param recruitmentId       모집 PK
     * @param userRecruitmentWish 모집 찜하기
     * @return BaseResponse
     */
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

    /**
     * 영화 찜하기
     *
     * @param userMovieId   유저 영화 PK
     * @param movieId       영화 PK
     * @param userMovieWish 찜 여부
     * @return BaseResponse
     */
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

    /**
     * 찜 목록 모집 게시물 가져오기
     *
     * @param pageable 페이징
     * @return BasePageResponse<UserRecruitmentListDto>
     */
    @GetMapping("/getRecruitmentList")
    public BasePageResponse<UserRecruitmentListDto> getRecruitmentList(@PageableDefault Pageable pageable) {

        BasePageResponse<UserRecruitmentListDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(wishService.getRecruitmentList(pageable, SecurityContextHolder.getContext().getAuthentication().getName()));

        return basePageResponse;
    }

    @GetMapping("/getMovieList")
    public BasePageResponse<MoviePageDto> getMovieList(@PageableDefault Pageable pageable){

        BasePageResponse<MoviePageDto> basePageResponse = new BasePageResponse<>();
        basePageResponse.setResult(true);
        basePageResponse.setPage(wishService.getMovieList(pageable, SecurityContextHolder.getContext().getAuthentication().getName()));

        return basePageResponse;
    }
}
