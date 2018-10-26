package com.coxandkings.travel.operations.utils.mailroomAndDispatch;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.MailRoomStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class MasterEnumSerializer extends StdSerializer<MailRoomStatus> {


    protected MasterEnumSerializer(){super(MailRoomStatus.class);}

    @Override
    public void serialize(MailRoomStatus value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if(value!=null)
            gen.writeString(value.getValue());

    }
}
