
package com.coxandkings.travel.operations.resource.failure.beresponse;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "paxType",
        "baseFare",
        "fees",
        "totalFare",
        "taxes"
})
public class BePaxTypeFare {

    @JsonProperty("paxType")
    private String paxType;
    @JsonProperty("baseFare")
    private BeBaseFare_ baseFare;
    @JsonProperty("fees")
    private BeFees_ fees;
    @JsonProperty("totalFare")
    private BeTotalFare totalFare;
    @JsonProperty("taxes")
    private BeTaxes_ taxes;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("paxType")
    public String getPaxType() {
        return paxType;
    }

    @JsonProperty("paxType")
    public void setPaxType(String paxType) {
        this.paxType = paxType;
    }

    @JsonProperty("baseFare")
    public BeBaseFare_ getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BeBaseFare_ baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("fees")
    public BeFees_ getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(BeFees_ fees) {
        this.fees = fees;
    }

    @JsonProperty("totalFare")
    public BeTotalFare getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(BeTotalFare totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("taxes")
    public BeTaxes_ getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(BeTaxes_ taxes) {
        this.taxes = taxes;
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
