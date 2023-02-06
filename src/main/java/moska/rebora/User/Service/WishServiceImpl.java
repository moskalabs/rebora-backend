package moska.rebora.User.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class WishServiceImpl implements WishService {

    UserRepository userRepository;
    RecruitmentRepository recruitmentRepository;
    UserRecruitmentRepository userRecruitmentRepository;
    MovieRepository movieRepository;
    UserMovieRepository userMovieRepository;

    /**
     * 모집 찜 변경
     *
     * @param userRecruitmentId   유저_모집 PK
     * @param recruitmentId       모집 PK
     * @param userEmail           유저 이메일
     * @param userRecruitmentWish 좋아요 여부
     */
    @Override
    public void wishRecruitment(Long userRecruitmentId, Long recruitmentId, String userEmail, Boolean userRecruitmentWish) {

        User user = userRepository.getUserByUserEmail(userEmail);
        UserRecruitment userRecruitment;

        //유저 모집이 없을 경우 유저 모집 생성
        if (userRecruitmentId == null) {
            Recruitment recruitment = recruitmentRepository.getRecruitmentById(recruitmentId);
            Optional<UserRecruitment> recruitmentOptional = userRecruitmentRepository.getUserRecruitmentByUserAndRecruitment(user, recruitment);

            //유저 모집이 없을 경우 유저 모집 생성
            if (recruitmentOptional.isEmpty()) {

                userRecruitment = UserRecruitment.builder()
                        .userRecruitmentWish(true)
                        .userRecruitmentYn(false)
                        .recruitment(recruitment)
                        .user(user)
                        .build();

                userRecruitmentRepository.save(userRecruitment);
            } else {

                //유저 모집 찜 변경
                userRecruitment = recruitmentOptional.get();
                userRecruitment.changeWish(userRecruitmentWish);
                userRecruitmentRepository.save(userRecruitment);
            }
        } else {

            //유저 모집 찜 변경
            userRecruitment = userRecruitmentRepository.getReferenceById(userRecruitmentId);
            userRecruitment.changeWish(userRecruitmentWish);
            userRecruitmentRepository.save(userRecruitment);
        }
    }

    /**
     * 영화 좋아요 누르기
     *
     * @param userMovieId   유저_영화_PK
     * @param movieId       영화_PK
     * @param userEmail     유저 이메일
     * @param userMovieWish 좋아요 여부
     */
    @Override
    public void wishMovie(Long userMovieId, Long movieId, String userEmail, Boolean userMovieWish) {

        User user = userRepository.getUserByUserEmail(userEmail);
        UserMovie userMovie;

        //유저 영화가 없을 경우
        if (userMovieId == null) {
            Movie movie = movieRepository.getMovieById(movieId);
            Optional<UserMovie> checkUserMovieOption = userMovieRepository.getUserMovieByUserAndMovie(user, movie);

            //유저 영화가 없을 경우
            if (checkUserMovieOption.isEmpty()) {
                userMovie = UserMovie
                        .builder()
                        .movie(movie)
                        .user(user)
                        .userMovieWish(true)
                        .build();

                userMovieRepository.save(userMovie);
            } else {

                //유저 영화 찜 변경
                userMovie = checkUserMovieOption.get();
                userMovie.changeWish(userMovieWish);
                userMovieRepository.save(userMovie);
            }
        } else {

            //유저 영화 찜 변경
            userMovie = userMovieRepository.getReferenceById(userMovieId);
            userMovie.changeWish(userMovieWish);
            userMovieRepository.save(userMovie);
        }
    }

    /**
     * 찜 목록 모집 게시물 가져오기
     *
     * @param pageable  페이징
     * @param userEmail 유저 이메일
     * @return Page<UserRecruitmentListDto>
     */
    @Override
    public Page<UserRecruitmentListDto> getRecruitmentList(Pageable pageable, String userEmail) {

        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUserRecruitmentWish(true); //찜한 것 만 가져오기

        return userRecruitmentRepository.getUserRecruitmentList(userEmail, pageable, userSearchCondition);
    }

    /**
     * 찜 목록 영화 리스트 가져오기
     *
     * @param pageable  페이징
     * @param userEmail 유저 이메이
     * @return Page<MoviePageDto>
     */
    @Override
    public Page<MoviePageDto> getMovieList(@Param("pageable") Pageable pageable,
                                           @Param("userEmail") String userEmail) {

        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUserMovieWish(true); //찜한 것 만 가져오기
        return userMovieRepository.getUserMovieList(userSearchCondition, userEmail, pageable);
    }
}
