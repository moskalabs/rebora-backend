package moska.rebora;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.opencsv.CSVReader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Admin.Dto.AdminRegionDto;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Cinema.Entity.Brand;
import moska.rebora.Cinema.Entity.BrandMovie;
import moska.rebora.Cinema.Entity.Cinema;
import moska.rebora.Cinema.Entity.MovieCinema;
import moska.rebora.Cinema.Repository.BrandMovieRepository;
import moska.rebora.Cinema.Repository.BrandRepository;
import moska.rebora.Cinema.Repository.CinemaRepository;
import moska.rebora.Cinema.Repository.MovieCinemaRepository;
import moska.rebora.Common.Dto.FCMMessage;
import moska.rebora.Common.Entity.Category;
import moska.rebora.Common.Repository.CategoryRepository;
import moska.rebora.Enum.*;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Movie.Entity.MovieCategory;
import moska.rebora.Movie.Repository.MovieCategoryRepository;
import moska.rebora.Movie.Repository.MovieRepository;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.DTO.ApplePublicKeyDto;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.Policy;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserMovie;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.PolicyRepository;
import moska.rebora.User.Repository.UserMovieRepository;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
import moska.rebora.User.Service.OathService;
import moska.rebora.User.Util.UserUtil;
import org.apiguardian.api.API;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static moska.rebora.Common.CommonConst.NAVER_TOKEN_ME_URL;


@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class ReboraApplicationTests {

    @Autowired
    RecruitmentRepository recruitmentRepository;

    @Test
    public void batchTest(){
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setRecruitmentStatus(RecruitmentStatus.CONFIRMATION);

        List<Recruitment> batchRecruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, userSearchCondition);

        batchRecruitmentList.stream().forEach(batch -> {
            log.info("recruitment={}", batch.getRecruitmentEndDate());
        });
    }
}
