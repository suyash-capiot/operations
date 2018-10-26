package com.coxandkings.travel.operations.resource.failure;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class TotalAmountDeserializer extends StdDeserializer<TotalAmountResource> {

    public TotalAmountDeserializer() {
        this(null);
    }

    public TotalAmountDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public TotalAmountResource deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Double amount = node.at("/totalPriceInfo/amount").doubleValue();
        String roomTypeCode = node.at("/roomInfo/roomTypeInfo/roomTypeCode").asText();
        String roomCategoryCode = node.at("/roomInfo/roomTypeInfo/roomCategoryCode").asText();
        String roomRef = node.at("/roomInfo/roomTypeInfo/roomRef").asText();
        String roomTypeName = node.at("/roomInfo/roomTypeInfo/roomTypeName").asText();
        String roomCategoryName = node.at("/roomInfo/roomTypeInfo/roomCategoryName").asText();
        return new TotalAmountResource(amount, roomTypeCode, roomCategoryCode, roomRef, roomTypeName, roomCategoryName);
    }
}
