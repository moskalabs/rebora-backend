package moska.rebora.Movie.Repository;

import moska.rebora.Admin.Repository.MovieAdmin;
import moska.rebora.Main.Repository.MovieRepositoryMain;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Recruitment.Entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryMain, MovieRepositoryCustom, MovieAdmin {

    Movie getMovieById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Movie m SET m.moviePopularCount = m.moviePopularCount/10")
    void updateMoviePopularCount();

    Movie getMovieByMovieName(String movieName);
}
