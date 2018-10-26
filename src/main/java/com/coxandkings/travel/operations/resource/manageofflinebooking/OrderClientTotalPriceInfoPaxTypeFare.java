package com.coxandkings.travel.operations.resource.manageofflinebooking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalFare",
    "paxType",
    "taxes",
    "baseFare",
    "fees",
    "clientEntityCommercial"
})
public class OrderClientTotalPriceInfoPaxTypeFare {

    @JsonProperty("totalFare")
    private TotalFare totalFare;
    @JsonProperty("paxType")
    private String paxType;
    @JsonProperty("taxes")
    private Taxes taxes;
    @JsonProperty("baseFare")
    private BaseFare baseFare;
    @JsonProperty("fees")
    private Fees fees;
    @JsonProperty("clientEntityCommercial")
    private List<ClientEntityCommercial> clientEntityCommercial = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("totalFare")
    public TotalFare getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(TotalFare totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("paxType")
    public String getPaxType() {
        return paxType;
    }

    @JsonProperty("paxType")
    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    @JsonProperty("taxes")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("baseFare")
    public BaseFare getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("fees")
    public Fees getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(Fees fees) {
        this.fees = fees;
    }

    @JsonProperty("clientEntityCommercial")
    public List<ClientEntityCommercial> getClientEntityCommercial() {
        return clientEntityCommercial;
    }

    @JsonProperty("clientEntityCommercial")
    public void setClientEntityCommercial(List<ClientEntityCommercial> clientEntityCommercial) {
        this.clientEntityCommercial = clientEntityCommercial;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
