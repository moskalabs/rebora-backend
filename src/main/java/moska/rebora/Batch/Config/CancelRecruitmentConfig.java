package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Configuration
public class CancelRecruitmentConfig {

    private RecruitmentRepository recruitmentRepository;

    @Bean
    public Job cancelRecruitmentJob(
            JobBuilderFactory jobBuilderFactory,
            Step cancelRecruitmentJobStep
    ) {
        log.info("********** This is cancelRecruitmentConfig");
        return jobBuilderFactory.get("cancelRecruitmentJob")  // 1_1
                .start(cancelRecruitmentJobStep)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step cancelRecruitmentJobStep(
            StepBuilderFactory stepBuilderFactory
    ) {
        log.info("********** This is cancelRecruitmentJobStep");
        return stepBuilderFactory.get("cancelRecruitmentJobStep")  // 2_1
                .<Recruitment, Recruitment>chunk(10)
                .reader(cancelRecruitmentReader())// 2_2
                .processor(cancelRecruitmentProcessor())
                .writer(cancelRecruitmentWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Recruitment> cancelRecruitmentReader() {
        log.info("********** This is cancelRecruitmentReader");
        UserSearchCondition condition = new UserSearchCondition();
        condition.setRecruitmentStatus(RecruitmentStatus.CANCEL);
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, condition);
        log.info("recruitmentList = {}", recruitmentList.size());
        return new ListItemReader<>(recruitmentList);
    }

    public ItemProcessor<Recruitment, Recruitment> cancelRecruitmentProcessor() {
        return new ItemProcessor<Recruitment, Recruitment>() {
            @Override
            public Recruitment process(Recruitment recruitment) throws Exception {
                log.info("********** This is cancelRecruitmentProcessor");
                recruitment.updateRecruitmentStatus(RecruitmentStatus.CANCEL);
                return recruitment;
            }
        };
    }

    public ItemWriter<Recruitment> cancelRecruitmentWriter() {
        log.info("********** This is cancelRecruitmentWriter");
        return ((List<? extends Recruitment> recruitmentList) -> recruitmentRepository.saveAll(recruitmentList));  // 1
    }
}
