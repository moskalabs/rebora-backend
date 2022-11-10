package moska.rebora.User.Service;

import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WishServiceImpl implements WishService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserMovieRepository userMovieRepository;

    @Override
    public void wishRecruitment(Long userRecruitmentId, Long recruitmentId, String userEmail, Boolean userRecruitmentWish) {

        User user = userRepository.getUserByUserEmail(userEmail);
        UserRecruitment userRecruitment;
        if (userRecruitmentId == null) {
            Recruitment recruitment = recruitmentRepository.getRecruitmentById(recruitmentId);
            userRecruitment = UserRecruitment.builder()
                    .userRecruitmentWish(true)
                    .userRecruitmentYn(false)
                    .recruitment(recruitment)
                    .user(user)
                    .build();

            userRecruitmentRepository.save(userRecruitment);
        } else {
            userRecruitment = userRecruitmentRepository.getReferenceById(userRecruitmentId);
            userRecruitment.changeWish(userRecruitmentWish);
            userRecruitmentRepository.save(userRecruitment);
        }
    }

    @Override
    public void wishMovie(Long userMovieId, Long movieId, String userEmail, Boolean userMovieWish) {
        User user = userRepository.getUserByUserEmail(userEmail);
        UserMovie userMovie;

        if (userMovieId == null) {
            Movie movie = movieRepository.getMovieById(movieId);
            userMovie = UserMovie
                    .builder()
                    .movie(movie)
                    .user(user)
                    .userMovieWish(true)
                    .build();

            userMovieRepository.save(userMovie);
        } else {
            userMovie = userMovieRepository.getReferenceById(userMovieId);
            userMovie.changeWish(userMovieWish);
            userMovieRepository.save(userMovie);
        }
    }
}
