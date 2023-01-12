package moska.rebora.User.Service;

import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.DTO.UserDto;
import moska.rebora.User.DTO.UserLoginDto;
import moska.rebora.User.Entity.User;
import org.springframework.data.repository.query.Param;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;

public interface UserService {

    UserLoginDto login(@Param("userEmail") String userEmail,
                       @Param("password") String password);

    UserLoginDto signUp(
            @Param("userEmail") String userEmail,
            @Param("password") String password,
            @Param("userName") String userName,
            @Param("userNickname") String userNickname,
            @Param("userPushYn") Boolean userPushYn,
            @Param("userPushNightYn") Boolean userPushNightYn,
            @Param("userPushKey") String userPushKey,
            @Param("authKey") String authKey,
            @Param("userSnsKind") String userSnsKind,
            @Param("userSnsId") String userSnsId
    ) throws SQLIntegrityConstraintViolationException;

    BaseResponse sendVerificationEmail(@Param("userEmail") String userEmail,
                                       @Param("emailAuthKind") EmailAuthKind emailAuthKind);

    UserDto getUserInfoByUserEmail(@Param("userEmail") @Valid String userEmail);

    BaseResponse checkRedundancyNickname(@Param("userNickname") String userNickname);

    BaseResponse changePassword(@Param("userEmail") String userEmail,
                                @Param("password") String password,
                                @Param("authKey") String authKey
    );

    boolean isOnValidUser(User user);
}
