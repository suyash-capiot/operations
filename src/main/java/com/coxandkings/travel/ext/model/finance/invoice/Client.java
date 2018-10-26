
package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientType",
    "clientId",
    "clientName",
    "clientAddress",
    "clientState",
    "clientCity",
    "clientPincode",
    "clientContactNo",
    "clientEmail"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {

    @JsonProperty("clientType")
    private String clientType;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientName")
    private String clientName;
    @JsonProperty("clientAddress")
    private String clientAddress;
    @JsonProperty("clientState")
    private String clientState;
    @JsonProperty("clientCity")
    private String clientCity;
    @JsonProperty("clientPincode")
    private Integer clientPincode;
    @JsonProperty("clientContactNo")
    private String clientContactNo;
    @JsonProperty("clientEmail")
    private String clientEmail;
    @JsonProperty("clientGstNumber")
    private String clientGstNumber;
    @JsonProperty("clientPanNumber")
    private String clientPanNumber;
    
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("clientType")
    public String getClientType() {
        return clientType;
    }

    @JsonProperty("clientType")
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @JsonProperty("clientId")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("clientName")
    public String getClientName() {
        return clientName;
    }

    @JsonProperty("clientName")
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @JsonProperty("clientAddress")
    public String getClientAddress() {
        return clientAddress;
    }

    @JsonProperty("clientAddress")
    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    @JsonProperty("clientState")
    public String getClientState() {
        return clientState;
    }

    @JsonProperty("clientState")
    public void setClientState(String clientState) {
        this.clientState = clientState;
    }

    @JsonProperty("clientCity")
    public String getClientCity() {
        return clientCity;
    }

    @JsonProperty("clientCity")
    public void setClientCity(String clientCity) {
        this.clientCity = clientCity;
    }

    @JsonProperty("clientPincode")
    public Integer getClientPincode() {
        return clientPincode;
    }

    @JsonProperty("clientPincode")
    public void setClientPincode(Integer clientPincode) {
        this.clientPincode = clientPincode;
    }

    @JsonProperty("clientContactNo")
    public String getClientContactNo() {
        return clientContactNo;
    }

    @JsonProperty("clientContactNo")
    public void setClientContactNo(String clientContactNo) {
        this.clientContactNo = clientContactNo;
    }

    @JsonProperty("clientEmail")
    public String getClientEmail() {
        return clientEmail;
    }

    @JsonProperty("clientEmail")
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getClientGstNumber() {
        return clientGstNumber;
    }

    public void setClientGstNumber(String clientGstNumber) {
        this.clientGstNumber = clientGstNumber;
    }

    public String getClientPanNumber() {
        return clientPanNumber;
    }

    public void setClientPanNumber(String clientPanNumber) {
        this.clientPanNumber = clientPanNumber;
    }

}
