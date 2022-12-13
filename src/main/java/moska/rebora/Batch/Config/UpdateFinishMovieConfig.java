package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Configuration
public class UpdateFinishMovieConfig {

    private RecruitmentRepository recruitmentRepository;

    @Bean
    public Job updateFinishMovieJob(
            JobBuilderFactory jobBuilderFactory,
            Step updateFinishMovieJobStep
    ) {
        log.info("********** This is updateFinishMovieJob");
        return jobBuilderFactory.get("updateFinishMovieJob")  // 1_1
                .start(updateFinishMovieJobStep)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step updateFinishMovieJobStep(
            StepBuilderFactory stepBuilderFactory
    ) {
        log.info("********** This is updateFinishMovieJobStep");
        return stepBuilderFactory.get("updateFinishMovieJobStep")
                .<Recruitment, Recruitment>chunk(10)
                .reader(updateFinishMovieReader())// 2_2
                .processor(updateFinishMovieProcessor())
                .writer(updateFinishMovieWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Recruitment> updateFinishMovieReader() {
        log.info("********** This is updateFinishMovieReader");
        UserSearchCondition condition = new UserSearchCondition();
        condition.setFinishTime(LocalDateTime.now());
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchFinishMovie();
        log.info("recruitmentList = {}", recruitmentList.size());
        return new ListItemReader<>(recruitmentList);
    }

    public ItemProcessor<Recruitment, Recruitment> updateFinishMovieProcessor() {
        log.info("********** This is updateFinishMovieProcessor");
        return new ItemProcessor<Recruitment, Recruitment>() {
            @Override
            public Recruitment process(Recruitment recruitment) throws Exception {
                recruitment.updateRecruitmentStatus(RecruitmentStatus.COMPLETED);
                return recruitment;
            }
        };
    }

    public ItemWriter<Recruitment> updateFinishMovieWriter() {
        log.info("********** This is updateFinishMovieWriter");
        return ((List<? extends Recruitment> recruitmentList) ->
                recruitmentRepository.saveAll(recruitmentList));  // 1
    }

}