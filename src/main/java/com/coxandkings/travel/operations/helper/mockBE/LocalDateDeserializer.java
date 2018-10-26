package com.coxandkings.travel.operations.helper.mockBE;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends StdDeserializer<ZonedDateTime> {

    protected LocalDateDeserializer() {
        super(ZonedDateTime.class);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonToken jsonToken = p.getCurrentToken();
        String opsDateFormat = "yyyy-MM-dd'T'HH:mm:ssXXX";

        if (jsonToken.equals(JsonToken.VALUE_STRING)) {
            String text = p.getText().trim();
            ZonedDateTime d = ZonedDateTime.parse(text, DateTimeFormatter.ofPattern(opsDateFormat));
            return d;
        }

        throw ctxt.mappingException("Date deserialize failed");
    }
}

