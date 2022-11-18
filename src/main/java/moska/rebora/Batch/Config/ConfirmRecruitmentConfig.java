package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
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
public class ConfirmRecruitmentConfig {

    private RecruitmentRepository recruitmentRepository;

    @Bean
    public Job confirmRecruitmentJob(
            JobBuilderFactory jobBuilderFactory,
            Step confirmRecruitmentJobStep
    ) {
        log.info("********** This is confirmRecruitmentConfig");
        return jobBuilderFactory.get("confirmRecruitmentJob")  // 1_1
                .start(confirmRecruitmentJobStep)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step confirmRecruitmentJobStep(
            StepBuilderFactory stepBuilderFactory
    ) {
        log.info("********** This is confirmRecruitmentJobStep");
        return stepBuilderFactory.get("confirmRecruitmentJobStep")  // 2_1
                .<Recruitment, Recruitment>chunk(10)
                .reader(confirmRecruitmentReader())// 2_2
                .processor(confirmRecruitmentProcessor())
                .writer(confirmRecruitmentWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Recruitment> confirmRecruitmentReader() {
        log.info("********** This is confirmRecruitmentReader");
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING);
        log.info("recruitmentList ={}", recruitmentList.size());
        return new ListItemReader<>(recruitmentList);
    }

    public ItemProcessor<Recruitment, Recruitment> confirmRecruitmentProcessor() {
        return new ItemProcessor<Recruitment, Recruitment>() {
            @Override
            public Recruitment process(Recruitment item) throws Exception {
                log.info("********** This is confirmRecruitmentProcessor");
                item.updateRecruitmentStatus(RecruitmentStatus.CONFIRMATION);
                return item;
            }
        };
    }

    public ItemWriter<Recruitment> confirmRecruitmentWriter() {
        log.info("********** This is confirmRecruitmentWriter");
        return ((List<? extends Recruitment> recruitmentList) ->
                recruitmentRepository.saveAll(recruitmentList));  // 1
    }
}
