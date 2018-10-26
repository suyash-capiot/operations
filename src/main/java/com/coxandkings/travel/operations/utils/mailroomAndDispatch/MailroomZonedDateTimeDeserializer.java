package com.coxandkings.travel.operations.utils.mailroomAndDispatch;

import com.coxandkings.travel.operations.utils.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MailroomZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    protected MailroomZonedDateTimeDeserializer(){
        super(ZonedDateTime.class);
    }

    Logger logger= LogManager.getLogger(MailroomZonedDateTimeDeserializer.class);

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date=p.getText();
        ZonedDateTime zonedDateTime = null;
        if (StringUtils.isEmpty(date))
            return zonedDateTime;


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            zonedDateTime = ZonedDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            logger.debug("unable to parse the given date");
            throw new IOException(Constants.ER371);
        }
        return zonedDateTime;
    }
}
