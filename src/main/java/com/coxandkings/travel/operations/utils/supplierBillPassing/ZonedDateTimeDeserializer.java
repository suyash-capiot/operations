package com.coxandkings.travel.operations.utils.supplierBillPassing;

import com.coxandkings.travel.operations.utils.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.Logger;import org.apache.logging.log4j.LogManager;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {
    protected ZonedDateTimeDeserializer() {
        super(ZonedDateTime.class);
    }

    Logger logger=LogManager.getLogger(ZonedDateTimeDeserializer.class);
    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date=p.getText();
        if (StringUtils.isEmpty(date)) throw new IOException(Constants.ER371);
        ZonedDateTime zonedDateTime = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz");
        try {
            zonedDateTime = ZonedDateTime.parse(date, dtf);
        } catch (Exception e) {
            logger.debug("unable to parse the given date");
            throw new IOException(Constants.ER371);
        }
       return zonedDateTime;
    }
}
