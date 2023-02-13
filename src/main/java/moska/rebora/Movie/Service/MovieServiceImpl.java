package moska.rebora.Movie.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Common.Repository.CategoryRepository;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    MovieRepository movieRepository;

    CategoryRepository categoryRepository;

    UserRepository userRepository;

    @Override
    public BasePageResponse<MoviePageDto> getList(UserSearchCondition searchCondition, String userEmail, Pageable pageable) {

        Page<MoviePageDto> movieDtoList;
        Category category = categoryRepository.getCategoryByCategoryName(searchCondition.getCategory());

        String userBirth = LocalDate.now().minusDays(20L).toString();
        if (!userEmail.equals("anonymousUser")) {
            User user = userRepository.getUserByUserEmail(userEmail);
            userBirth = user.getUserBirth();
        }

        if (category == null) {
            movieDtoList = movieRepository.getMovieList(searchCondition, userEmail, userBirth, pageable);
        } else {
            movieDtoList = movieRepository.getMovieListByCategory(searchCondition, userEmail, userBirth, pageable, category);
        }

        BasePageResponse<MoviePageDto> moviePage = new BasePageResponse<>();
        moviePage.setPage(movieDtoList);
        moviePage.setResult(true);
        return moviePage;
    }
}
