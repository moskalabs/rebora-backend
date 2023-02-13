package moska.rebora.Admin.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Admin.Dto.AdminUserDto;
import moska.rebora.User.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    UserRepository userRepository;

    @Override
    public AdminUserDto getUserInfo(Long userId) {

        AdminUserDto adminUserDto;

        adminUserDto = userRepository.getAdminUserInfo(userId);
        adminUserDto.setWishMovieList(userRepository.getWishMovieList(userId));
        adminUserDto.setWishRecruitmentList(userRepository.getWishRecruitmentList(userId));

        return adminUserDto;
    }
}
