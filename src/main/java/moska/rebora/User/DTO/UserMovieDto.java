package moska.rebora.User.DTO;

import lombok.Getter;
import lombok.Setter;
import moska.rebora.User.Entity.UserMovie;

@Getter
@Setter
public class UserMovieDto {

    private Long id;
    private Boolean userMovieWish;

    public UserMovieDto(UserMovie userMovie) {
        this.id = userMovie.getId();
        this.userMovieWish = userMovie.getUserMovieWish();
    }
}
