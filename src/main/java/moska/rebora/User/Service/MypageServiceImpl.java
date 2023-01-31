package moska.rebora.User.Service;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Common.Service.FileUploadService;
import moska.rebora.Common.Util;
import moska.rebora.User.DTO.MypageInfoDto;
import moska.rebora.User.DTO.MypageUpdateDto;
import moska.rebora.User.DTO.UserRecruitmentListDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import net.minidev.json.JSONObject;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;

@Service
@Slf4j
public class MypageServiceImpl implements MypageService {

    @Autowired
    UserRecruitmentRepository userRecruitmentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    Util util;

    @Autowired
    FileUploadService fileUploadService;

    /**
     * 마이페이지 정보 가져오기
     * 참여모집내역 개수, 내가 한 모집글 개수
     *
     * @param userEmail 유저 이메일
     * @return JSONObject
     */
    @Override
    public MypageInfoDto info(@Param("userEmail") String userEmail) {
        MypageInfoDto mypageInfoDto = new MypageInfoDto();

        mypageInfoDto.setResult(true);
        mypageInfoDto.setCountParticipationHistory(userRecruitmentRepository.countParticipationHistory(userEmail));
        mypageInfoDto.setCountMyRecruiter(userRecruitmentRepository.countMyRecruiter(userEmail));
        return mypageInfoDto;
    }

    /**
     * 내가 참여한 모집 내역 가져오기
     *
     * @param pageable 페이징
     * @return UserRecruitmentListDto
     */
    @Override
    public Page<UserRecruitmentListDto> getParticipationHistory(Pageable pageable) {

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUserRecruitmentYn(true);
        userSearchCondition.setTheaterStartDatetime(false);

        return userRecruitmentRepository.getUserRecruitmentList(userEmail, pageable, userSearchCondition);
    }

    /**
     * 내가 모집한 모집글 가져오기
     *
     * @param pageable 페이징
     * @return Page<UserRecruitmentListDto>
     */
    @Override
    public Page<UserRecruitmentListDto> getMyRecruiter(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setCreateByMe(true);
        Page<UserRecruitmentListDto> userRecruitmentListDtoPage = userRecruitmentRepository.getUserRecruitmentList(userEmail, pageable, userSearchCondition);
        return userRecruitmentListDtoPage;
    }

    /**
     * 유저 푸쉬 변경
     *
     * @param userId     유저 아이디
     * @param userPushYn 유저 푸쉬 여부
     * @param userEmail  유저 이메일
     */
    @Override
    public void updatePushYn(@Param("userId") Long userId,
                             @Param("userPushYn") Boolean userPushYn,
                             @Param("userEmail") String userEmail,
                             @Param("userPushKey") String userPushKey
    ) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new JwtException("옳바르지 않은 접근입니다.");
        }
        User user = userOptional.get();

        if (user.getUserEmail().equals(userEmail)) {
            user.changePushYn(userPushYn, userPushKey);
            userRepository.save(user);
        } else {
            throw new JwtException("옳바르지 않은 접근입니다.");
        }
    }

    @Override
    public void updatePushNightYn(Long userId, Boolean userPushNightYn, String userEmail, String userPushKey) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new JwtException("옳바르지 않은 접근입니다.");
        }

        User user = userOptional.get();

        if (user.getUserEmail().equals(userEmail)) {
            user.changePushNightYn(userPushNightYn, userPushKey);
            userRepository.save(user);
        } else {
            throw new JwtException("옳바르지 않은 접근입니다.");
        }
    }

    /**
     * 내정보 변경
     *
     * @param userId          유저 아이디
     * @param userEmail       유저 이메일
     * @param mypageUpdateDto 내정보 업데이트 DTO
     * @return BaseResponse
     * @throws SQLIntegrityConstraintViolationException 중복된 경우 Exception
     */
    @Override
    public BaseResponse changeMyInfo(Long userId,
                                     String userEmail,
                                     MypageUpdateDto mypageUpdateDto) throws SQLIntegrityConstraintViolationException {

        try {

            User user = userRepository.getUserByUserEmail(userEmail);

            if (!certifyUser(userId, user)) {
                throw new JwtException("옳바르지 않은 접근입니다.");
            }

            String fileUrl = user.getUserImage();

            String password = ""; //패스워드
            if (!mypageUpdateDto.getChangePassword().isEmpty()) {
                if (!passwordEncoder.matches(mypageUpdateDto.getCurrentPassword(), user.getPassword())) {
                    throw new JwtException("비밀번호가 맞지 않습니다.");
                }
                password = passwordEncoder.encode(mypageUpdateDto.getChangePassword());
            }

            if (mypageUpdateDto.getFile() != null) {
                MultipartFile file = mypageUpdateDto.getFile();
                String originalFileName = file.getOriginalFilename(); //원본 파일 이름
                String ext = FilenameUtils.getExtension(originalFileName); //확장자
                String newFileName = "user/" + userId + "/" + userId + "_" + util.createRandomString(8) + "." + ext; //새로운 파일 이름
                fileUrl = fileUploadService.uploadImage(file, newFileName); //파일 Url
            }

            if (fileUrl == null) {
                fileUrl = "";
            }

            user.changeUserInfo(fileUrl, password, mypageUpdateDto.getUserNickname());

            log.info("userNickname={}", user.getUserNickname());
            log.info("password={}", user.getPassword());
            log.info("userImage={}", user.getUserImage());
            userRepository.save(user);

            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setResult(true);

            return baseResponse;
        } catch (DataIntegrityViolationException e) {
            throw new SQLIntegrityConstraintViolationException("중복된 닉네임입니다.");
        }
    }

    public Boolean certifyUser(Long userId, User user) {
        return userId.equals(user.getId());
    }
}
