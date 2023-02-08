package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Banner.Dto.BannerCompareDto;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Repository.MainBannerRepository;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.UserRecruitment;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Configuration
public class CreateMainBannerConfig {

    private BannerRepository bannerRepository;

    private MainBannerRepository mainBannerRepository;

    JobBuilderFactory jobBuilderFactory;

    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job createMainBannerJob(
            Step createMainBannerJobStep
    ) {
        log.info("********** This is createMainBannerConfig");
        return jobBuilderFactory.get("createMainBannerJob")  // 1_1
                .start(createMainBannerJobStep)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step createMainBannerJobStep(
    ) {
        log.info("********** 메인 배너 생성 배치 confirmRecruitmentJobStep **********");
        return stepBuilderFactory.get("createMainBannerJobStep")  // 2_1
                .<BannerCompareDto, MainBanner>chunk(30)
                .reader(createMainBannerReader())// 2_2
                .processor(createMainBannerProcessor())
                .writer(createMainBannerWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<BannerCompareDto> createMainBannerReader() {
        log.info("********** 메인 배너 생성 배치 createMainBannerReader **********");
        List<BannerCompareDto> bannerCompareDtoList = bannerRepository.getCompareBannerList();
        log.info("recruitmentList = {}", bannerCompareDtoList.size());
        return new ListItemReader<>(bannerCompareDtoList);
    }

    public ItemProcessor<BannerCompareDto, MainBanner> createMainBannerProcessor() {
        return new ItemProcessor<BannerCompareDto, MainBanner>() {
            @Override
            public MainBanner process(BannerCompareDto bannerCompareDto) throws Exception {
                log.info("********** 메인 배너 생성 배치 createMainBannerProcessor **********");
                mainBannerRepository.deleteAll();
                return MainBanner
                        .builder()
                        .banner(bannerRepository.findById(bannerCompareDto.getBannerId()).get())
                        .build();
            }
        };
    }

    public ItemWriter<MainBanner> createMainBannerWriter() {
        log.info("********** 메인 배너 생성 배치 createMainBannerWriter **********");
        return ((List<? extends MainBanner> mainBannerList) -> mainBannerRepository.saveAll(mainBannerList));  // 1
    }
}
