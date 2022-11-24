package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
import moska.rebora.User.Repository.UserRepository;
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
public class ConfirmRecruitmentConfig {

    private RecruitmentRepository recruitmentRepository;

    private UserRecruitmentRepository userRecruitmentRepository;

    private PaymentRepository paymentRepository;

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
        UserSearchCondition condition = new UserSearchCondition();
        condition.setRecruitmentStatus(RecruitmentStatus.CANCEL);
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, condition);
        log.info("recruitmentList = {}", recruitmentList.size());
        return new ListItemReader<>(recruitmentList);
    }

    public ItemProcessor<Recruitment, Recruitment> confirmRecruitmentProcessor() {
        return new ItemProcessor<Recruitment, Recruitment>() {
            @Override
            public Recruitment process(Recruitment recruitment) throws Exception {
                log.info("********** This is confirmRecruitmentProcessor");
                List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getUserRecruitmentByRecruitment(recruitment);
                for (UserRecruitment userRecruitment : userRecruitmentList) {
                    log.info("userEmail = {}", userRecruitment.getUser().getUserEmail());
                    String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(userRecruitment.getRecruitment().getId());
                    stringBuilder.append("_");
                    stringBuilder.append(userRecruitment.getUser().getId());
                    stringBuilder.append("_");
                    stringBuilder.append(now);

                    Payment payment = Payment.builder()
                            .id(stringBuilder.toString())
                            .paymentKey("paymentKey")
                            .paymentContent("예약 결제")
                            .paymentAmount(7000)
                            .paymentMethod(PaymentMethod.CARD)
                            .paymentStatus(PaymentStatus.COMPLETE)
                            .userRecruitment(userRecruitment)
                            .build();

                    paymentRepository.save(payment);
                }
                recruitment.updateRecruitmentStatus(RecruitmentStatus.CONFIRMATION);
                return recruitment;
            }
        };
    }

    public ItemWriter<Recruitment> confirmRecruitmentWriter() {
        log.info("********** This is confirmRecruitmentWriter");
        return ((List<? extends Recruitment> recruitmentList) ->
                recruitmentRepository.saveAll(recruitmentList));  // 1
    }
}
