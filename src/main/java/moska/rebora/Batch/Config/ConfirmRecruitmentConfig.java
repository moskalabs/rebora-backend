package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentMethod;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
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

//모집 확인
@Slf4j
@AllArgsConstructor
@Configuration
public class ConfirmRecruitmentConfig {

    private RecruitmentRepository recruitmentRepository;

    private UserRecruitmentRepository userRecruitmentRepository;

    private PaymentRepository paymentRepository;

    private PaymentService paymentService;

    NotificationService notificationService;

    JobBuilderFactory jobBuilderFactory;

    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job confirmRecruitmentJob(
            Step confirmRecruitmentJobStep
    ) {
        log.info("********** 상영 확정 confirmRecruitmentConfig **********");
        return jobBuilderFactory.get("confirmRecruitmentJob")  // 1_1
                .start(confirmRecruitmentJobStep)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step confirmRecruitmentJobStep(
    ) {
        log.info("********** 상영 확정 배치 confirmRecruitmentJobStep **********");
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
        log.info("********** 상영 확정 배치 confirmRecruitmentReader **********");
        UserSearchCondition condition = new UserSearchCondition();
        condition.setRecruitmentStatus(RecruitmentStatus.CONFIRMATION);
        List<Recruitment> recruitmentList = recruitmentRepository.getBatchRecruitmentList(RecruitmentStatus.RECRUITING, condition);
        log.info("recruitmentList = {}", recruitmentList.size());
        return new ListItemReader<>(recruitmentList);
    }

    public ItemProcessor<Recruitment, Recruitment> confirmRecruitmentProcessor() {
        return new ItemProcessor<Recruitment, Recruitment>() {
            @Override
            public Recruitment process(Recruitment recruitment) throws Exception {
                log.info("********** 상영 확정 배치 confirmRecruitmentProcessor **********");

                Movie movie = recruitment.getMovie();
                Theater theater = recruitment.getTheater();
                List<UserRecruitment> userWishRecruitmentList = userRecruitmentRepository.getBatchUserWishRecruitment(recruitment.getId());

                paymentService.paymentByRecruitment(recruitment);

                for (UserRecruitment userRecruitment : userWishRecruitmentList) {

                    User user = userRecruitment.getUser();
                    String notificationContent = notificationService.createNotificationContent(
                            movie.getMovieName(),
                            theater.getTheaterStartDatetime(),
                            theater.getTheaterDay(),
                            theater.getTheaterCinemaBrandName(),
                            theater.getTheaterCinemaName(),
                            theater.getTheaterName()
                    );

                    String notificationSubject = "찜한 모집의 상영이 확정되었습니다.";
                    notificationService.createNotificationRecruitment(notificationSubject, notificationContent, NotificationKind.CONFORMATION, recruitment, user);
                }

                recruitment.updateRecruitmentStatus(RecruitmentStatus.CONFIRMATION);
                return recruitment;
            }
        };
    }

    public ItemWriter<Recruitment> confirmRecruitmentWriter() {
        log.info("********** 상영 확정 배치 confirmRecruitmentWriter **********");
        return ((List<? extends Recruitment> recruitmentList) ->
                recruitmentRepository.saveAll(recruitmentList));  // 1
    }
}
