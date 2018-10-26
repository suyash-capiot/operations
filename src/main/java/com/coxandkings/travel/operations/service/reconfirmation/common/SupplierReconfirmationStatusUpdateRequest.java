package com.coxandkings.travel.operations.service.reconfirmation.common;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userID",
        "orderID",
        "suppReconfirmStatus"
})
public class SupplierReconfirmationStatusUpdateRequest {

    @JsonProperty("userID")
    public String userID;
    @JsonProperty("orderID")
    public String orderID;
    @JsonProperty("suppReconfirmStatus")
    public String suppReconfirmStatus;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getSuppReconfirmStatus() {
        return suppReconfirmStatus;
    }

    public void setSuppReconfirmStatus(String suppReconfirmStatus) {
        this.suppReconfirmStatus = suppReconfirmStatus;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}