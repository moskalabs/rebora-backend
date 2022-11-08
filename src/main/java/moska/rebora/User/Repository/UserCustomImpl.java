package moska.rebora.User.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import moska.rebora.User.Entity.User;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static moska.rebora.User.Entity.QUser.user;

public class UserCustomImpl implements UserCustom{

    private final JPAQueryFactory queryFactory;

    public UserCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<User> getTestUserList(Pageable pageable){
        return queryFactory.select(user).from(user).offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();
    }
}
