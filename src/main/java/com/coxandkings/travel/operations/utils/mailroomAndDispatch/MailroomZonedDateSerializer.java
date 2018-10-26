package com.coxandkings.travel.operations.utils.mailroomAndDispatch;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MailroomZonedDateSerializer extends StdSerializer<ZonedDateTime> {

    protected MailroomZonedDateSerializer(){
        super(ZonedDateTime.class);
    }

    Logger logger= LogManager.getLogger(MailroomZonedDateTimeDeserializer.class);

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value!=null)
            gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")));
//            gen.writeString(value.format(DateTimeFormatter.ISO_DATE_TIME));

//
    }
}
