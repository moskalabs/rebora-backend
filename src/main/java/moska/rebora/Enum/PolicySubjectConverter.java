package moska.rebora.Enum;

import org.springframework.core.convert.converter.Converter;

public class PolicySubjectConverter implements Converter<String, PolicySubject>{

    @Override
    public PolicySubject convert(String kindCode) {
        return PolicySubject.valueOf(kindCode);
    }
}
