package com.coxandkings.travel.operations.helper.remarks;

import com.coxandkings.travel.operations.resource.remarks.UserBasicInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class UserDeserializer extends StdDeserializer<UserBasicInfo> {

    public UserDeserializer()
    {
        this(null);
    }

    public UserDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UserBasicInfo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String firstName = node.at("/userDetails/firstName").asText();
        String lastName = node.at("/userDetails/lastName").asText();
        String email=node.at("/userDetails/email").asText();
        String id= node.findValue("_id").asText();
        return new UserBasicInfo(id, firstName+" "+lastName,email);
    }
}
