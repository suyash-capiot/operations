package com.coxandkings.travel.operations.resource.timelimitbooking.convert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "clientID",
        "clientType",
        "clientLanguage",
        "clientMarket",
        "clientCurrency"
})
public class TimeLimitClientContext {

    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("clientType")
    private String clientType;
    @JsonProperty("clientLanguage")
    private String clientLanguage;
    @JsonProperty("clientMarket")
    private String clientMarket;
    @JsonProperty("clientCurrency")
    private String clientCurrency;

    @JsonProperty("clientID")
    public String getClientID() {
        return clientID;
    }

    @JsonProperty("clientID")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("clientType")
    public String getClientType() {
        return clientType;
    }

    @JsonProperty("clientType")
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @JsonProperty("clientLanguage")
    public String getClientLanguage() {
        return clientLanguage;
    }

    @JsonProperty("clientLanguage")
    public void setClientLanguage(String clientLanguage) {
        this.clientLanguage = clientLanguage;
    }

    @JsonProperty("clientMarket")
    public String getClientMarket() {
        return clientMarket;
    }

    @JsonProperty("clientMarket")
    public void setClientMarket(String clientMarket) {
        this.clientMarket = clientMarket;
    }

    @JsonProperty("clientCurrency")
    public String getClientCurrency() {
        return clientCurrency;
    }

    @JsonProperty("clientCurrency")
    public void setClientCurrency(String clientCurrency) {
        this.clientCurrency = clientCurrency;
    }
}
