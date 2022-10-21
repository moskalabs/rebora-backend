package moska.rebora.User.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Movie.Entity.Movie;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_movie_id", nullable = false)
    private Long id;

    @Column
    private Boolean user_movie_wish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    public UserMovie(Boolean user_movie_wish, User user, Movie movie) {
        this.user_movie_wish = user_movie_wish;
        this.user = user;
        this.movie = movie;
    }
}
