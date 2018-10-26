package com.coxandkings.travel.operations.model.prepaymenttosupplier.loader;

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
        "lastUpdated",
        "typeOfSettlement",
        "settlementOn",
        "commercialHead",
        "_id",
        "noCreditSettlement",
        "creditSettlement",
        "commission"
})
public class CommercialHead {

    @JsonProperty("lastUpdated")
    private String lastUpdated;
    @JsonProperty("typeOfSettlement")
    private String typeOfSettlement;
    @JsonProperty("settlementOn")
    private String settlementOn;
    @JsonProperty("commercialHead")
    private String commercialHead;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("noCreditSettlement")
    private List<NoCreditSettlement> noCreditSettlement = null;
    @JsonProperty("creditSettlement")
    private List<Object> creditSettlement = null;
    @JsonProperty("commission")
    private Commission commission;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("lastUpdated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("typeOfSettlement")
    public String getTypeOfSettlement() {
        return typeOfSettlement;
    }

    @JsonProperty("typeOfSettlement")
    public void setTypeOfSettlement(String typeOfSettlement) {
        this.typeOfSettlement = typeOfSettlement;
    }

    @JsonProperty("settlementOn")
    public String getSettlementOn() {
        return settlementOn;
    }

    @JsonProperty("settlementOn")
    public void setSettlementOn(String settlementOn) {
        this.settlementOn = settlementOn;
    }

    @JsonProperty("commercialHead")
    public String getCommercialHead() {
        return commercialHead;
    }

    @JsonProperty("commercialHead")
    public void setCommercialHead(String commercialHead) {
        this.commercialHead = commercialHead;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("noCreditSettlement")
    public List<NoCreditSettlement> getNoCreditSettlement() {
        return noCreditSettlement;
    }

    @JsonProperty("noCreditSettlement")
    public void setNoCreditSettlement(List<NoCreditSettlement> noCreditSettlement) {
        this.noCreditSettlement = noCreditSettlement;
    }

    @JsonProperty("creditSettlement")
    public List<Object> getCreditSettlement() {
        return creditSettlement;
    }

    @JsonProperty("creditSettlement")
    public void setCreditSettlement(List<Object> creditSettlement) {
        this.creditSettlement = creditSettlement;
    }

    @JsonProperty("commission")
    public Commission getCommission() {
        return commission;
    }

    @JsonProperty("commission")
    public void setCommission(Commission commission) {
        this.commission = commission;
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