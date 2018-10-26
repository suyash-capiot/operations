package com.coxandkings.travel.operations.config;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.Level;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<java.time.ZonedDateTime, java.sql.Timestamp> {

    private static final Logger log = LogManager.getLogger(ZonedDateTimeConverter.class.getName());
    private static final Level LEVEL = Level.FINEST;


    @Override
    public java.sql.Timestamp convertToDatabaseColumn(java.time.ZonedDateTime attribute) {
        Timestamp timestamp = null;
//        log.info( "convertToDatabaseColumn({0})", attribute);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss[.]");
        if (attribute != null) {
            timestamp = Timestamp.from(attribute.toInstant());
            simpleDateFormat.format(timestamp);
        }
        return (attribute == null) ? null : timestamp;
    }

    @Override
    public java.time.ZonedDateTime convertToEntityAttribute(java.sql.Timestamp dbData) {
        LocalDateTime withoutTimezone = null;
        ZonedDateTime withTimezone = null;
//        log.info( "convertToEntityAttribute({0})", dbData);

        if (dbData != null) {
            withoutTimezone = dbData.toLocalDateTime();
            withTimezone = withoutTimezone.atZone(ZoneId.systemDefault());
        }
        return dbData == null ? null : withTimezone;

    }


}