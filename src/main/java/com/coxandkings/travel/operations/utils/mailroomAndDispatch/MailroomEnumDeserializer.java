package com.coxandkings.travel.operations.utils.mailroomAndDispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class MailroomEnumDeserializer extends StdDeserializer<MailRoomStatus> {

    protected MailroomEnumDeserializer(){super(MailRoomStatus.class);}

    Logger logger= LogManager.getLogger(MailroomEnumDeserializer.class);

    @Override
    public MailRoomStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String str=p.getText();
        if (StringUtils.isEmpty(str))
        {
            return MailRoomStatus.EMPTY;
        }
        else
        {
            return MailRoomStatus.valueOf(str);
        }

    }
}
