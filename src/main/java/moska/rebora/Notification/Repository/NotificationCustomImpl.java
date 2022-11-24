package moska.rebora.Notification.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.Notification.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.Notification.Entity.QNotification.notification;
import static moska.rebora.Payment.Entity.QPayment.payment;
import static moska.rebora.Recruitment.Entity.QRecruitment.recruitment;
import static moska.rebora.User.Entity.QUser.user;

public class NotificationCustomImpl implements NotificationCustom {

    private JPAQueryFactory queryFactory;

    public NotificationCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NotificationDto> getNotificationPage(Pageable pageable, String userEmail) {
        List<NotificationDto> content = queryFactory
                .select(Projections.fields(
                        NotificationDto.class,
                        notification.id.as("notificationId"),
                        notification.notificationSubject,
                        notification.notificationContent,
                        notification.notificationKind,
                        notification.notificationReadYn,
                        notification.regDate,
                        notification.modDate,
                        recruitment.id.as("recruitmentId"),
                        payment.paymentStatus,
                        payment.paymentAmount
                ))
                .from(notification)
                .join(notification.user, user)
                .leftJoin(notification.recruitment, recruitment)
                .leftJoin(notification.payment, payment)
                .where(user.userEmail.eq(userEmail))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> total = queryFactory.select(notification.id.count())
                .from(notification)
                .join(notification.user, user)
                .where(user.userEmail.eq(userEmail));

        return PageableExecutionUtils.getPage(content, pageable, total::fetchFirst);
    }


}
