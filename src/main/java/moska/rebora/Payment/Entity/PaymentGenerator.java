package moska.rebora.Payment.Entity;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.spi.Configurable;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PaymentGenerator extends SequenceStyleGenerator {
    // 속성값 처리 메소드 override

    public static final String USER_ID = "userId";
    public static final String RECRUITMENT_ID = "recruitmentId";
    private String recruitmentId;
    private String userId;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        userId = ConfigurationHelper.getString(USER_ID, params);
        recruitmentId = ConfigurationHelper.getString(RECRUITMENT_ID, params);

        log.info("userId = {}", userId);
        log.info("recruitmentId = {}", recruitmentId);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        String newId = "Hello";

        return newId;
    }
}