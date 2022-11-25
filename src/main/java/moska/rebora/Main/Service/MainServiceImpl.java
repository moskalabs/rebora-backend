package moska.rebora.Main.Service;

import moska.rebora.Banner.Dto.BannerDto;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Main.Dto.MainDto;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    MainBannerRepository mainBannerRepository;

    @Override
    public MainDto getMain() {

        MainDto mainDto = new MainDto();

        UserSearchCondition userSearchCondition = new UserSearchCondition(); //검색 조건
        Pageable pageable = PageRequest.of(0, 10);
        userSearchCondition.setRecruitmentStatus(RecruitmentStatus.CONFIRMATION); //확정 된 경우에만
        userSearchCondition.setOrderByMovie("popular"); //인기순으로 정리

        JSONObject jsonObject = new JSONObject();
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        mainDto.setResult(true);
        mainDto.setRecruitmentList(recruitmentRepository.getRecruitmentMainList(userEmail));
        mainDto.setMovieList(movieRepository.getMovieMainList(userEmail));

        List<BannerDto> bannerRecentlyDtoList = mainBannerRepository.getMainBanner();
        List<BannerDto> bannerDtoList = new ArrayList<>();
        if (bannerRecentlyDtoList.isEmpty()) {
            Banner banner = bannerRepository.findById(1L).get();
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
