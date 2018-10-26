package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpsPaxTypeFareClient extends OpsPaxTypeFare {

    @JsonProperty("clientCommercials")
    private List<OpsPaxRoomClientCommercial> clientCommercials = new ArrayList<OpsPaxRoomClientCommercial>();

    public OpsPaxTypeFareClient() {
    }

    public List<OpsPaxRoomClientCommercial> getClientCommercials() {
        return clientCommercials;
    }

    public void setClientCommercials(List<OpsPaxRoomClientCommercial> clientCommercials) {
        this.clientCommercials = clientCommercials;
    }
}
