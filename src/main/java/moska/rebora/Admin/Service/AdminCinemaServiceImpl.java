package moska.rebora.Admin.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Cinema.Dto.CinemaPageDto;
import moska.rebora.Cinema.Dto.MovieCinemaDto;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Entity.MovieCinema;
import moska.rebora.Cinema.Repository.CinemaRepository;
import moska.rebora.Cinema.Repository.MovieCinemaRepository;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AdminCinemaServiceImpl implements AdminCinemaService {

    CinemaRepository cinemaRepository;

    MovieRepository movieRepository;

    MovieCinemaRepository movieCinemaRepository;
    TheaterRepository theaterRepository;

    @Override
    public Page<CinemaPageDto> getCinemaPage(Pageable pageable, UserSearchCondition userSearchCondition) {
        return cinemaRepository.getCinemaPage(pageable, userSearchCondition);
    }

    @Override
    public CinemaPageDto getCinemaInfo(Long cinemaId) {
        CinemaPageDto cinemaPageDto = new CinemaPageDto();
        List<MovieCinemaDto> cinemaDtoList = new ArrayList<>();
        List<Movie> movieList = movieRepository.findAll();
        cinemaDtoList = movieList.stream().map(MovieCinemaDto::new).collect(Collectors.toList());
        if (cinemaId != null) {
            cinemaPageDto = cinemaRepository.getCinemaInfo(cinemaId);
            List<MovieCinemaDto> cinemaSearchList = movieCinemaRepository.getMovieCinemaByCinemaId(cinemaId);
            cinemaDtoList.forEach(movieCinemaDto -> {
                cinemaSearchList.forEach(movieCinemaDto1 -> {
                    if (movieCinemaDto.getMovieName().equals(movieCinemaDto1.getMovieName())) {
                        movieCinemaDto.setMovieCinemaYn(true);
                    }
                });
            });
        }

        cinemaPageDto.setMovieList(cinemaDtoList);

        return cinemaPageDto;
    }

    @Override
    public void saveInfo(Long cinemaId, String brandName, String regionName, String cinemaName, List<Long> movieIdList) {

        if (cinemaId != null) {
            Optional<Cinema> optionalCinema = cinemaRepository.findById(cinemaId);

            if (optionalCinema.isEmpty()) {
                throw new NullPointerException("존재하지 않는 극장입니다.");
            }

            Cinema cinema = optionalCinema.get();
            List<Theater> theaterList = theaterRepository.getTheatersByCinemaId(cinemaId);

            if (!brandName.equals(cinema.getBrandName())) {
                cinema.updateBrandName(brandName);
                for (Theater theater : theaterList) {
                    theater.updateBrandName(brandName);
                }
            }

            if (!regionName.equals(cinema.getRegionName())) {
                cinema.updateRegionName(regionName);
                for (Theater theater : theaterList) {
                    theater.updateRegionName(regionName);
                }
            }

            if (!cinemaName.equals(cinema.getCinemaName())) {
                cinema.updateCinemaName(cinemaName);
                for (Theater theater : theaterList) {
                    theater.updateCinemaName(cinemaName);
                }
            }

            List<MovieCinema> movieCinemaList = movieCinemaRepository.getMovieCinemasByCinemaId(cinemaId);
            if (movieCinemaList.size() != 0) {
                movieCinemaRepository.deleteAll(movieCinemaList);
            }

            List<MovieCinema> movieCinemas = new ArrayList<>();
            List<Movie> movieList = movieRepository.getMoviesByIdIn(movieIdList);

            for (Movie movie : movieList) {
                MovieCinema movieCinema = MovieCinema
                        .builder()
                        .movie(movie)
                        .cinema(cinema)
                        .build();

                movieCinemas.add(movieCinema);
            }

            theaterRepository.saveAll(theaterList);
            cinemaRepository.save(cinema);
            movieCinemaRepository.saveAll(movieCinemas);
        } else {
            Cinema cinema = Cinema
                    .builder()
                    .regionName(regionName)
                    .cinemaUseYn(true)
                    .brandName(brandName)
                    .cinemaName(cinemaName)
                    .build();

            cinemaRepository.save(cinema);

            List<MovieCinema> movieCinemas = new ArrayList<>();
            List<Movie> movieList = movieRepository.getMoviesByIdIn(movieIdList);

            for (Movie movie : movieList) {
                MovieCinema movieCinema = MovieCinema
                        .builder()
                        .movie(movie)
                        .cinema(cinema)
                        .build();

                movieCinemas.add(movieCinema);
            }

            movieCinemaRepository.saveAll(movieCinemas);
        }
    }
}
