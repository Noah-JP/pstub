package com.gt.stub.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by noah on 2017. 5. 20..
 */
@Converter(autoApply = true)
public class BooleanConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute.toString();
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return Boolean.valueOf(dbData);
    }
}
