package moska.rebora.User.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moska.rebora.Common.BaseTimeEntity;
import moska.rebora.Movie.Entity.Movie;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMovie extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_movie_id", nullable = false)
    private Long id;

    @Column
    private Boolean userMovieWish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public void changeWish(Boolean userMovieWish){
        this.userMovieWish = userMovieWish;
    }

    @Builder
    public UserMovie(Boolean userMovieWish, User user, Movie movie) {
        this.userMovieWish = userMovieWish;
        this.user = user;
        this.movie = movie;
    }
}
