package moska.rebora.User.Service;

import lombok.NoArgsConstructor;
import moska.rebora.Common.Util;
import moska.rebora.Enum.EmailAuthKind;
import moska.rebora.User.Entity.UserEmailAuth;
import moska.rebora.User.Repository.UserEmailAuthRepository;
import moska.rebora.User.Repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.Optional;

import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

@Service
@NoArgsConstructor
public class UserEmailServiceImpl implements UserEmailAuthService {

    @Autowired
    UserEmailAuthRepository userEmailAuthRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
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

        if (userEmailAuthOptional.isEmpty()) {
            throw new NullPointerException("이메일 인증이 옳바르지 않습니다. 다시 시도해주세요");
        } else {
            UserEmailAuth userEmailAuth = userEmailAuthOptional.get();
            if (!passwordEncoder.matches(userEmail, authKey)) {
                throw new CredentialsExpiredException("이메일 인증이 옳바르지 않습니다. 인증을 다시 시도해주세요");
            }
            if (userEmailAuth.getExpireDate().isBefore(ChronoLocalDateTime.from(LocalDateTime.now().atZone(ZoneId.systemDefault())))) {
                throw new CredentialsExpiredException("이메일 인증시간이 만료 되었습니다. 인증을 다시 시도해주세요");
            }

            return userEmailAuth;
        }
    }

    /**
     * 유저 회원가입 이메일 발송
     *
     * @param userEmail    유저 이메일
     * @param verifyNumber 인증 번호
     */
    public void sendSignUpEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber) {

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
    public void sendPasswordEmail(@Param("userEmail") String userEmail, @Param("verifyNumber") String verifyNumber) {
        int userCount = userRepository.countUSerByUserEmail(userEmail);
        if (userCount == 0) {
            throw new NullPointerException("존재하지 않는 이메일입니다.");
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
            userEmailAuth.sendEmailAuth(userEmail, verifyNumber, true, EmailAuthKind.SIGNUP);
        } else {
            userEmailAuth = UserEmailAuth.builder()
                    .email(userEmail)
                    .verifyNumber(verifyNumber)
                    .expireYn(true)
                    .emailAuthKind(EmailAuthKind.SIGNUP)
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
    public JSONObject validationEmailCode(@Param("userEmail") String userEmail,
                                          @Param("verifyNumber") String verifyNumber,
                                          @Param("emailAuthKind") EmailAuthKind emailAuthKind

    ) {
        JSONObject jsonObject = new JSONObject();
        Optional<UserEmailAuth> userEmailAuthOptional = userEmailAuthRepository.getUserEmailAuthByEmailAndExpireYnAndEmailAuthKind(userEmail, true, emailAuthKind);
        try {
            if (userEmailAuthOptional.isEmpty()) {
                throw new NullPointerException("인증 도중 오류가 발생하였습니다. 메일 인증을 다시 시도해주세요.");
            }

            UserEmailAuth userEmailAuth = userEmailAuthOptional.get();
            if (passwordEncoder.matches(verifyNumber, userEmailAuth.getVerifyNumber())) {

                String authKey = passwordEncoder.encode(userEmail);
                userEmailAuth.updateAuthKey(authKey);
                userEmailAuthRepository.save(userEmailAuth);
                jsonObject.put("result", true);
                jsonObject.put("errorCode", null);
                jsonObject.put("message", null);
                jsonObject.put("authKey", authKey);

            } else {
                throw new NullPointerException("인증 번호가 일치하지 않습니다. 다시 시도해주세요");
            }
        } catch (NullPointerException e) {
            jsonObject.put("result", false);
            jsonObject.put("errorCode", "500");
            jsonObject.put("message", e.getMessage());
        }

        return jsonObject;
    }


}
