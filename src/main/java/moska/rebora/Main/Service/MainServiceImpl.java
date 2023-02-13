package moska.rebora.Main.Service;

import lombok.AllArgsConstructor;
import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Main.Dto.MainDto;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Repository.UserRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {

    RecruitmentRepository recruitmentRepository;

    MovieRepository movieRepository;

    BannerRepository bannerRepository;

    MainBannerRepository mainBannerRepository;
    private final UserRepository userRepository;

    @Override
    public MainDto getMain() {

        MainDto mainDto = new MainDto();

        UserSearchCondition userSearchCondition = new UserSearchCondition(); //검색 조건
        userSearchCondition.setRecruitmentStatus(RecruitmentStatus.CONFIRMATION); //확정 된 경우에만
        userSearchCondition.setOrderByMovie("popular"); //인기순으로 정리

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String userBirth = LocalDate.now().minusDays(20L).toString();

        if (!userEmail.equals("anonymousUser")) {
            User user = userRepository.getUserByUserEmail(userEmail);
            userBirth = user.getUserBirth();
        }

        mainDto.setResult(true);
        mainDto.setRecruitmentList(recruitmentRepository.getRecruitmentMainList(userEmail, userBirth));
        mainDto.setMovieList(movieRepository.getMovieMainList(userEmail, userBirth));

        List<BannerDto> bannerRecentlyDtoList = mainBannerRepository.getMainBanner();
        List<BannerDto> bannerDtoList = new ArrayList<>();
        if (bannerRecentlyDtoList.isEmpty()) {
            Banner banner = bannerRepository.getBannerById(1L);
            BannerDto bannerDto = new BannerDto();
            bannerDto.setBannerExposeYn(banner.getBannerExposeYn());
            bannerDto.setBannerImage(banner.getBannerImage());
            bannerDto.setBannerMainText(banner.getBannerMainText());
            bannerDto.setBannerSubText(banner.getBannerSubText());
            bannerDtoList.add(bannerDto);
        } else {
            Collections.shuffle(bannerRecentlyDtoList);
            for (int i = 0; i < bannerRecentlyDtoList.size(); i++) {
                if (i > 2) {
                    break;
                }
                bannerDtoList.add(bannerRecentlyDtoList.get(i));
            }
        }
        mainDto.setBannerList(bannerDtoList);

        return mainDto;
    }
}
