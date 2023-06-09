package moska.rebora.User.Service;

import com.mchange.util.DuplicateElementException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import moska.rebora.Common.Util;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.DTO.UserEmailDto;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserEmailAuth;
import moska.rebora.User.Repository.UserEmailAuthRepository;
import moska.rebora.User.Repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserEmailServiceImpl implements UserEmailAuthService {

    UserEmailAuthRepository userEmailAuthRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    Util util;

    /**
     * 유저 이메일 인증 체크
     *
     * @param userEmail     유저 이메일
     * @param authKey       인증 키
     * @param emailAuthKind 인증 종류
     * @return UserEmailAuth
     */
    @Override
    public UserEmailAuth checkUserEmailAuth(String userEmail, String authKey, EmailAuthKind emailAuthKind) {
        Optional<UserEmailAuth> userEmailAuthOptional =
                userEmailAuthRepository.getUserEmailAuthByEmailAndExpireYnAndEmailAuthKind(userEmail, true, emailAuthKind);

        //이메일 인증 받은 적이 없을 경우
        if (userEmailAuthOptional.isEmpty()) {
            throw new NullPointerException("이메일 인증이 옳바르지 않습니다. 다시 시도해주세요");


        } else {
            UserEmailAuth userEmailAuth = userEmailAuthOptional.get();
            //이메일 인증이 올바르지 않을 경우
            if (!passwordEncoder.matches(userEmail, authKey)) {
                throw new CredentialsExpiredException("이메일 인증이 옳바르지 않습니다. 인증을 다시 시도해주세요");
            }
//            if (LocalDateTime.now().atZone(ZoneId.systemDefault()).isBefore(ChronoZonedDateTime.from(userEmailAuth.getExpireDate().atZone(ZoneId.systemDefault())))) {
//                throw new CredentialsExpiredException("이메일 인증시간이 만료 되었습니다. 인증을 다시 시도해주세요");
//            }

            return userEmailAuth;
        }
    }

    /**
     * 유저 회원가입 이메일 발송
     *
     * @param userEmail    유저 이메일
     * @param verifyNumber 인증 번호
     */
    @Transactional
    public void sendSignUpEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber) {

        userEmailAuthRepository.updateExposeFalse(userEmail, EmailAuthKind.SIGNUP);
        String subject = "리보라 인증 메일입니다.";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("리보라 인증 번호입니다. \n");
        stringBuilder.append(verifyNumber);
        util.sendEmail(userEmail, subject, stringBuilder.toString());

        createUserEmailAuth(userEmail, passwordEncoder.encode(verifyNumber), EmailAuthKind.SIGNUP);
    }

    /**
     * 유저 비밀번호 변경 메일 발송
     *
     * @param userEmail    유저 이메일
     * @param verifyNumber 인증 번호
     */
    @Transactional
    public void sendPasswordEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber) {
        userEmailAuthRepository.updateExposeFalse(userEmail, EmailAuthKind.PASSWORD);
        int userCount = userRepository.countUSerByUserEmail(userEmail);
        if (userCount == 0) {
            throw new NullPointerException("입력하신 이메일을 찾을 수가 없습니다.");
        }

        String subject = "리보라 비밀번호 변경 인증 메일입니다.";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("리보라 비밀변호 변경 인증 번호입니다. \n");
        stringBuilder.append(verifyNumber);
        util.sendEmail(userEmail, subject, stringBuilder.toString());

        createUserEmailAuth(userEmail, passwordEncoder.encode(verifyNumber), EmailAuthKind.PASSWORD);
    }

    /**
     * 인증 생성
     *
     * @param userEmail     유저 이메일
     * @param verifyNumber  인증 번호
     * @param emailAuthKind 인증 종류
     */
    public void createUserEmailAuth(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber, EmailAuthKind emailAuthKind) {
        Optional<UserEmailAuth> userEmailAuthOptional = userEmailAuthRepository.getUserEmailAuthByEmailAndExpireYnAndEmailAuthKind(userEmail, true, emailAuthKind);
        UserEmailAuth userEmailAuth;
        if (userEmailAuthOptional.isPresent()) {
            userEmailAuth = userEmailAuthOptional.get();
            userEmailAuth.sendEmailAuth(userEmail, verifyNumber, true, userEmailAuth.getEmailAuthKind());
        } else {
            userEmailAuth = UserEmailAuth.builder()
                    .email(userEmail)
                    .verifyNumber(verifyNumber)
                    .expireYn(true)
                    .emailAuthKind(emailAuthKind)
                    .build();
        }
        userEmailAuthRepository.save(userEmailAuth);
    }

    /**
     * 이메일 인증
     *
     * @param userEmail     유저 이메일
     * @param verifyNumber  인증 번호
     * @param emailAuthKind 인증 종류
     * @return JSONObject
     */
    @Override
    public UserEmailDto validationEmailCode(@Param("userEmail") String userEmail,
                                            @Param("verifyNumber") String verifyNumber,
                                            @Param("emailAuthKind") EmailAuthKind emailAuthKind

    ) {

        UserEmailDto userEmailDto = new UserEmailDto();
        Optional<UserEmailAuth> userEmailAuthOptional = userEmailAuthRepository.getUserEmailAuthByEmailAndExpireYnAndEmailAuthKind(userEmail, true, emailAuthKind);

        //이메일 인증이 없을 경우
        if (userEmailAuthOptional.isEmpty()) {
            throw new NullPointerException("인증 도중 오류가 발생하였습니다. 메일 인증을 다시 시도해주세요.");
        }

        UserEmailAuth userEmailAuth = userEmailAuthOptional.get();

        //이메일 인증 비밀번호 맞는지 매칭
        if (passwordEncoder.matches(verifyNumber, userEmailAuth.getVerifyNumber())) {

            String authKey = passwordEncoder.encode(userEmail);
            userEmailAuth.updateAuthKey(authKey);
            userEmailAuthRepository.save(userEmailAuth);
            userEmailDto.setResult(true);
            userEmailDto.setAuthKey(authKey);

        } else {
            throw new NullPointerException("인증 번호가 일치하지 않습니다. 다시 시도해주세요");
        }


        return userEmailDto;
    }


}
