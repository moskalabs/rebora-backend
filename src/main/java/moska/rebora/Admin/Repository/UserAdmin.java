package moska.rebora.Admin.Repository;

import moska.rebora.Admin.Dto.AdminUserDto;
import moska.rebora.Admin.Dto.AdminWishMovieDto;
import moska.rebora.Admin.Dto.AdminWishRecruitmentDto;
import moska.rebora.Enum.UserGrade;
import moska.rebora.Enum.UserSnsKind;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserAdmin {

    Page<AdminUserDto> getAdminUserPage(
            @Param("userSearchCondition") UserSearchCondition userSearchCondition,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("userGrade") UserGrade userGrade,
            @Param("userSnsKind") String userSnsKind,
            Pageable pageable
    );

    AdminUserDto getAdminUserInfo(@Param("userId") Long userId);

    List<AdminWishMovieDto> getWishMovieList(@Param("userId") Long userId);
    List<AdminWishRecruitmentDto> getWishRecruitmentList(@Param("userId") Long userId);
}
