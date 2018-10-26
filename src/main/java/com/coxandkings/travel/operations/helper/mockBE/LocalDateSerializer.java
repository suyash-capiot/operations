package com.coxandkings.travel.operations.helper.mockBE;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends StdSerializer<ZonedDateTime> {
    public LocalDateSerializer(){
        super(ZonedDateTime.class);
    }

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String opsDateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX";
        String outputTS = "";
        if (value != null)
            outputTS = value.format(DateTimeFormatter.ofPattern(opsDateFormat));
        gen.writeString( outputTS );
    }
}
