package moska.rebora.User.Repository;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

public interface UserRecruitmentCustom {

    public Long countParticipationHistory(@Param("userEmail") @Valid String userEmail);

    public Long countMyRecruiter(@Param("userEmail") @Valid String userEmail);
}
