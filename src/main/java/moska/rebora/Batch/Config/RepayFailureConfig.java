package moska.rebora.Batch.Config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moska.rebora.Banner.Entity.Banner;
import moska.rebora.Common.BaseResponse;
import moska.rebora.Enum.NotificationKind;
import moska.rebora.Enum.PaymentStatus;
import moska.rebora.Enum.RecruitmentStatus;
import moska.rebora.Movie.Entity.Movie;
import moska.rebora.Notification.Service.NotificationService;
import moska.rebora.Payment.Entity.Payment;
import moska.rebora.Payment.Entity.PaymentLog;
import moska.rebora.Payment.Repository.PaymentLogRepository;
import moska.rebora.Payment.Repository.PaymentRepository;
import moska.rebora.Payment.Service.PaymentService;
import moska.rebora.Recruitment.Entity.Recruitment;
import moska.rebora.Recruitment.Repository.RecruitmentRepository;
import moska.rebora.Theater.Entity.Theater;
import moska.rebora.User.DTO.UserSearchCondition;
import moska.rebora.User.Entity.User;
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

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Configuration
public class RepayFailureConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private PaymentRepository paymentRepository;

    private PaymentLogRepository paymentLogRepository;

    private PaymentService paymentService;

    private RecruitmentRepository recruitmentRepository;

    private NotificationService notificationService;

    @Bean
    public Job repayFailureJob(
            Step cancelRecruitmentJobStep
    ) {
        log.info("********** 결제 실패 재결제 배치 RepayFailureConfig **********");
        return jobBuilderFactory.get("repayFailureJob")  // 1_1
                .start(cancelRecruitmentJobStep)  // 1_3
                .build();  // 1_4
    }

    @Bean
    public Step repayFailureJobStop(
    ) {
        log.info("********** 결제 실패 재결제 배치 repayFailureJobStop **********");
        return stepBuilderFactory.get("repayFailureJobStop")  // 2_1
                .<Payment, Payment>chunk(10)
                .reader(repayFailureReader())// 2_2
                .processor(repayFailureProcessor())
                .writer(repayFailureWriter())
                .build();
    }

    @Bean
    @StepScope
    @Transactional
    public ListItemReader<Payment> repayFailureReader() {
        log.info("********** 결제 실패 재결제 배치 repayFailureReader **********");
        UserSearchCondition condition = new UserSearchCondition();
        List<Payment> paymentList = paymentRepository.getBatchPaymentList(PaymentStatus.FAILURE);

        return new ListItemReader<>(paymentList);
    }

    public ItemProcessor<Payment, Payment> repayFailureProcessor() {
        return new ItemProcessor<Payment, Payment>() {
            @Override
            public Payment process(Payment payment) throws Exception {
                log.info("********** 결제 실패 재결제 배치 repayFailureProcessor **********");

                UserRecruitment userRecruitment = payment.getUserRecruitment();
                Recruitment recruitment = payment.getUserRecruitment().getRecruitment();
                Movie movie = recruitment.getMovie();
                Theater theater = recruitment.getTheater();
                User user = userRecruitment.getUser();
                String notificationSubject = "모집 참여가 완료되었습니다.";

                payment = paymentService.getBatchPayment(user, recruitment, payment, theater, userRecruitment, movie);

                String notificationContent = notificationService.createNotificationContent(
                        movie.getMovieName(),
                        theater.getTheaterStartDatetime(),
                        theater.getTheaterDay(),
                        theater.getTheaterCinemaBrandName(),
                        theater.getTheaterCinemaName(),
                        theater.getTheaterName()
                );

                if (payment.getPaymentStatus() == PaymentStatus.COMPLETE) {
                    notificationService.createNotificationPayment(notificationSubject, notificationContent, NotificationKind.CONFORMATION, user, recruitment, payment);
                    PaymentLog paymentLog = PaymentLog.builder()
                            .payment(payment)
                            .paymentCardNumber(payment.getPaymentCardNumber())
                            .paymentLogStatus(payment.getPaymentStatus())
                            .paymentLogContent(payment.getPaymentContent())
                            .paymentLogAmount(payment.getPaymentAmount())
                            .paymentLogCardCode(payment.getPaymentCardCode())
                            .paymentMethod(payment.getPaymentMethod())
                            .paidAt(payment.getPaidAt())
                            .receiptUrl(payment.getReceiptUrl())
                            .build();

                    paymentLogRepository.save(paymentLog);
                    recruitment.plusRecruitmentPeople(userRecruitment.getUserRecruitmentPeople());
                    recruitmentRepository.save(recruitment);
                }

                if (payment.getPaymentStatus() == PaymentStatus.FAILURE) {
                    notificationSubject = "참여한 모집의 결제가 실패하였습니다.";
                    PaymentLog paymentLog = PaymentLog.builder()
                            .paymentLogStatus(payment.getPaymentStatus())
                            .payment(payment)
                            .paymentLogContent(payment.getPaymentContent())
                            .build();
                    notificationService.createNotificationPayment(notificationSubject, payment.getPaymentContent(), NotificationKind.CONFORMATION, user, recruitment, payment);
                    paymentLogRepository.save(paymentLog);
                }

                return payment;
            }
        };
    }

    public ItemWriter<Payment> repayFailureWriter() {
        log.info("********** 결제 실패 재결제 배치 repayFailureWriter **********");
        return ((List<? extends Payment> paymentList) ->
                paymentRepository.saveAll(paymentList));  // 1
    }

}
