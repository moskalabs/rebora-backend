package moska.rebora.Admin.Service;

import moska.rebora.Admin.Dto.AdminUserDto;

public interface AdminUserService {

    AdminUserDto getUserInfo(Long userId);
}
