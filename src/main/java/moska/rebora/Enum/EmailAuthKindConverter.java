package moska.rebora.Enum;

import org.springframework.core.convert.converter.Converter;

public class EmailAuthKindConverter implements Converter<String, EmailAuthKind> {

    @Override
    public EmailAuthKind convert(String kindCode) {
        return EmailAuthKind.valueOf(kindCode);
    }
}
