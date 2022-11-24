package moska.rebora.Batch.Schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Movie.Repository.MovieRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@AllArgsConstructor
public class UpdateMovieCountSch {

    private MovieRepository movieRepository;

    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void updateMovieCount(){
        movieRepository.updateMoviePopularCount();
    }
}
