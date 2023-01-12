package moska.rebora.Admin.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Admin.Dto.AdminPaymentDto;
import moska.rebora.Admin.Dto.QAdminPaymentDto;
import moska.rebora.Admin.Dto.QAdminTheaterDto;
import moska.rebora.Payment.Dto.PaymentLogDto;
import moska.rebora.User.DTO.UserSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static moska.rebora.Movie.Entity.QMovie.movie;
import static moska.rebora.Payment.Entity.QPayment.payment;
import static moska.rebora.Payment.Entity.QPaymentLog.paymentLog;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.User.Entity.QUser.user;
import static moska.rebora.User.Entity.QUserRecruitment.userRecruitment;

public class PaymentAdminRepositoryImpl implements PaymentAdminRepository {

    private final JPAQueryFactory queryFactory;

    public PaymentAdminRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<AdminPaymentDto> getPaymentDto(
            Pageable pageable,
            UserSearchCondition userSearchCondition,
            LocalDateTime fromDateTime,
            LocalDateTime toDateTime
    ) {

        List<AdminPaymentDto> content = queryFactory.select(
                        new QAdminPaymentDto(
                                payment.id.as("paymentId"),
                                payment.paymentContent,
                                payment.paymentAmount,
                                payment.paymentMethod,
                                payment.paymentStatus,
                                payment.paymentCardCode,
                                payment.pgProvider,
                                payment.paymentCardName,
                                payment.paidAt,
                                payment.paymentCardNumber,
                                payment.paymentMemo,
                                payment.receiptUrl,
                                user.userEmail,
                                user.userNickname,
                                user.userName,
                                user.id.as("userId"),
                                movie.id.as("movieId"),
                                movie.movieName,
                                recruitment.id.as("recruitmentId")
                        ))
                .from(payment)
                .join(payment.userRecruitment, userRecruitment)
                .join(userRecruitment.user, user)
                .join(userRecruitment.recruitment, recruitment)
                .join(recruitment.movie, movie)
                .where(
                        getSearchWord(userSearchCondition.getSearchWord(), userSearchCondition.getSearchCondition()),
                        getSearchDate(fromDateTime, toDateTime)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(payment.paidAt.desc())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(
                        payment.count()
                )
                .from(payment)
                .join(payment.userRecruitment, userRecruitment)
                .join(userRecruitment.user, user)
                .join(userRecruitment.recruitment, recruitment)
                .join(recruitment.movie, movie)
                .where(
                        getSearchWord(userSearchCondition.getSearchWord(), userSearchCondition.getSearchCondition()),
                        getSearchDate(fromDateTime, toDateTime)
                );

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }

    @Override
    public AdminPaymentDto getPaymentInfo(String paymentId) {
        AdminPaymentDto adminPaymentDto = queryFactory.select(
                        new QAdminPaymentDto(
                                payment.id.as("paymentId"),
                                payment.paymentContent,
                                payment.paymentAmount,
                                payment.paymentMethod,
                                payment.paymentStatus,
                                payment.paymentCardCode,
                                payment.pgProvider,
                                payment.paymentCardName,
                                payment.paidAt,
                                payment.paymentCardNumber,
                                payment.paymentMemo,
                                payment.receiptUrl,
                                user.userEmail,
                                user.userNickname,
                                user.userName,
                                user.id.as("userId"),
                                movie.id.as("movieId"),
                                movie.movieName,
                                recruitment.id.as("recruitmentId")
                        ))
                .from(payment)
                .join(payment.userRecruitment, userRecruitment)
                .join(userRecruitment.user, user)
                .join(userRecruitment.recruitment, recruitment)
                .join(recruitment.movie, movie)
                .where(payment.id.eq(paymentId))
                .fetchFirst();

        adminPaymentDto.setPaymentLogDtoList(getPaymentLogList(paymentId));

        return adminPaymentDto;
    }

    public List<PaymentLogDto> getPaymentLogList(String paymentId) {
        return queryFactory.select(
                        Projections.fields(
                                PaymentLogDto.class,
                                paymentLog.id.as("paymentLogId"),
                                paymentLog.paymentLogContent,
                                paymentLog.paymentLogAmount,
                                paymentLog.paymentMethod,
                                paymentLog.paymentLogStatus,
                                paymentLog.paymentLogCardCode,
                                paymentLog.pgProvider,
                                paymentLog.paidAt,
                                paymentLog.receiptUrl,
                                paymentLog.paymentCardNumber,
                                paymentLog.regDate
                        ))
                .from(paymentLog)
                .where(paymentLog.payment.id.eq(paymentId))
                .orderBy(paymentLog.paidAt.desc())
                .fetch();
    }

    public BooleanExpression getSearchWord(String searchWord, String searchCondition) {
        switch (searchCondition) {
            case "userNickname":
                return user.userNickname.contains(searchWord);
            case "userName":
                return user.userName.contains(searchWord);
            case "movieName":
                return movie.movieName.contains(searchWord);
            default:
                return null;
        }
    }

    public BooleanExpression getSearchDate(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
        return payment.paidAt.between(fromDateTime, toDateTime);
    }
}
