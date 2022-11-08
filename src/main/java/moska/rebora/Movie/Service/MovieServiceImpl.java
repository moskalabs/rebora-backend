package moska.rebora.Movie.Service;

import moska.rebora.Common.BasePageResponse;
import moska.rebora.Movie.Dto.MovieDto;
import moska.rebora.Movie.Dto.MoviePageDto;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService{

    @Autowired
    MovieRepository movieRepository;

    @Override
    public BasePageResponse<MoviePageDto> getList(UserSearchCondition searchCondition, String userEmail, Pageable pageable) {
        JSONObject jsonObject = new JSONObject();
        Page<MoviePageDto> movieDtoList = movieRepository.getMovieList(searchCondition, userEmail, pageable);
        BasePageResponse<MoviePageDto> moviePage = new BasePageResponse<MoviePageDto>();
        moviePage.setPage(movieDtoList);
        moviePage.setResult(true);
        return moviePage;
    }
}
