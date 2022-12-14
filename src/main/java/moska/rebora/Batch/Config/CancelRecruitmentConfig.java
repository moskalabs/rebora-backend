package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Banner.Repository.BannerRepository;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
import moska.rebora.User.Entity.UserRecruitment;
import moska.rebora.User.Repository.UserRecruitmentRepository;
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

    private UserRecruitmentRepository userRecruitmentRepository;

    BannerRepository bannerRepository;

    NotificationService notificationService;

    private PaymentService paymentService;

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

                List<UserRecruitment> userRecruitmentList = userRecruitmentRepository.getBatchRefundUserRecruitment(recruitment.getId());
                List<UserRecruitment> userWishRecruitmentList = userRecruitmentRepository.getBatchUserWishRecruitment(recruitment.getId());

                Movie movie = recruitment.getMovie();
                Theater theater = recruitment.getTheater();
                String notificationSubject = "참여한 모집이 취소되었습니다.";

                for (UserRecruitment userRecruitment : userRecruitmentList) {

                    Payment payment = userRecruitment.getPayment();
                    BaseResponse baseResponse = paymentService.paymentCancel(payment);
                    User user = userRecruitment.getUser();

                    String notificationContent = notificationService.createNotificationContent(
                            movie.getMovieName(),
                            theater.getTheaterStartDatetime(),
                            theater.getTheaterDay(),
                            theater.getTheaterCinemaBrandName(),
                            theater.getTheaterCinemaName(),
                            theater.getTheaterName()
                    );

                    if (baseResponse.getResult()) {
                        notificationService.createNotificationPayment(notificationSubject, notificationContent, NotificationKind.CANCEL, user, recruitment, payment);
                    }

                }

                Banner banner = bannerRepository.getBannerByRecruitment(recruitment);

                if (banner != null) {
                    banner.deleteBanner();
                    bannerRepository.save(banner);
                }

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
                    notificationSubject = "찜한 모집의 상영이 취소되었습니다.";
                    notificationService.createNotificationRecruitment(notificationSubject, notificationContent, NotificationKind.CANCEL, recruitment, user);
                }

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
