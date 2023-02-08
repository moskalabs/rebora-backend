package moska.rebora.Movie.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Common.BasePageResponse;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Common.Repository.CategoryRepository;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService{
    MovieRepository movieRepository;

    CategoryRepository categoryRepository;

    @Override
    public BasePageResponse<MoviePageDto> getList(UserSearchCondition searchCondition, String userEmail, Pageable pageable) {

        Page<MoviePageDto> movieDtoList;
        Category category = categoryRepository.getCategoryByCategoryName(searchCondition.getCategory());

        if(category == null){
            movieDtoList = movieRepository.getMovieList(searchCondition, userEmail, pageable);
        }else{
            movieDtoList = movieRepository.getMovieListByCategory(searchCondition, userEmail, pageable, category);
        }

        BasePageResponse<MoviePageDto> moviePage = new BasePageResponse<MoviePageDto>();
        moviePage.setPage(movieDtoList);
        moviePage.setResult(true);
        return moviePage;
    }
}
