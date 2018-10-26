package com.coxandkings.travel.operations.helper.remarks;

import com.coxandkings.travel.operations.resource.remarks.RoleBasicInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class RoleDeserializer extends StdDeserializer<RoleBasicInfo>{


    public RoleDeserializer()
    {
        this(null);
    }

    public RoleDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RoleBasicInfo deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Boolean deleted=node.at("/deleted").asBoolean();
        String company = node.at("/company").asText();
        String roleName = node.at("/roleName").asText();
        String id= node.findValue("_id").asText();
        if(deleted)
            return null;
        else
            return new RoleBasicInfo(id, company,roleName);
    }
}
