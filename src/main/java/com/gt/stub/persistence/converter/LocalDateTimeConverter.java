package com.gt.stub.persistence.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by noah on 2017. 5. 20..
 */
@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public java.sql.Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
        return Timestamp.valueOf(entityValue);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(java.sql.Timestamp databaseValue) {
        return databaseValue.toLocalDateTime();
    }
}
