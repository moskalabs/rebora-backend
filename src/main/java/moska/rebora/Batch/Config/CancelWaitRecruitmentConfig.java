package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Banner.Dto.BannerCompareDto;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Entity.MainBanner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Banner.Service.BannerService;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.Theater.Repository.TheaterRepository;
import moska.rebora.User.DTO.UserSearchCondition;
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

import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;

@Slf4j
@AllArgsConstructor
@Configuration
public class CancelWaitRecruitmentConfig {

    RecruitmentRepository recruitmentRepository;

    BannerRepository bannerRepository;

    BannerService bannerService;

    TheaterRepository theaterRepository;

    JobBuilderFactory jobBuilderFactory;

    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job cancelWaitRecruitmentJob(
            Step cancelWaitRecruitmentJobStop) {
        log.info("********** 모집 대기 취소 배치 cancelWaitRecruitment **********");
        return jobBuilderFactory.get("cancelRecruitmentJob")  // 1_1
                .start(cancelWaitRecruitmentJobStop)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step cancelWaitRecruitmentJobStop() {
        log.info("********** 메인 배너 생성 배치 confirmRecruitmentJobStep **********");
        return stepBuilderFactory.get("cancelWaitRecruitmentJobStop")  // 2_1
                .<Recruitment, Recruitment>chunk(10)
                .reader(cancelWaitRecruitmentReader())// 2_2
                .processor(cancelWaitRecruitmentProcessor())
                .writer(cancelWaitRecruitmentWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Recruitment> cancelWaitRecruitmentReader() {
        log.info("********** 모집 대기 취소 배치 cancelWaitRecruitmentReader **********");
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchWaitRecruitmentList();
        log.info("recruitment 사이즈 = {}", recruitmentList.size());

        return new ListItemReader<>(recruitmentList);
    }

    public ItemProcessor<Recruitment, Recruitment> cancelWaitRecruitmentProcessor() {
        log.info("********** 모집 대기 취소 배치 cancelWaitRecruitmentWriter **********");
        return new ItemProcessor<Recruitment, Recruitment>() {
            public Recruitment process(Recruitment recruitment) throws Exception {
                Banner banner = bannerRepository.getBannerByRecruitment(recruitment);

                Theater theater = recruitment.getTheater();
                theater.addRecruitment(null);

                bannerService.bannerDelete(banner);

                theaterRepository.save(theater);

                return recruitment;
            }
        };
    }

    public ItemWriter<Recruitment> cancelWaitRecruitmentWriter() {
        log.info("********** 모집 취소 배치 cancelWaitRecruitmentWriter **********");
        return ((List<? extends Recruitment> recruitmentList) -> recruitmentRepository.deleteAll(recruitmentList));  // 1
    }

}
