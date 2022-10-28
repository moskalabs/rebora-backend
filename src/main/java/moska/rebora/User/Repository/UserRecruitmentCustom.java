package moska.rebora.User.Repository;

import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

public interface UserRecruitmentCustom {

    Long countParticipationHistory(@Param("userEmail") @Valid String userEmail);

    Long countMyRecruiter(@Param("userEmail") @Valid String userEmail);

    Page<UserRecruitment> getParticipationHistory(@Param("userEmail") @Valid String userEmail,
                                                  Pageable pageable);
}
