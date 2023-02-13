package moska.rebora.Main.Repository;

import moska.rebora.User.DTO.UserRecruitmentListDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentRepositoryMain {

    List<UserRecruitmentListDto> getRecruitmentMainList(
            @Param("userEmail")String userEmail,
            @Param("userBirth")String userBirth
    );
}
