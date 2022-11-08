package moska.rebora.User.Repository;

import moska.rebora.User.Entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserCustom {

    public List<User> getTestUserList(Pageable pageable);
}
