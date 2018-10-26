
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "passengerType",
        "fareBreakUp",
        "commercialDetails",
        "totalReceivables",
        "totalPayables",
        "commercialsApplied",
        "totalFare",
        "entityCommercials"
})
public class BrmsClientPassengerDetail {

    @JsonProperty("passengerType")
    private String passengerType;
    @JsonProperty("commercialDetails")
    private List<BrmsClientCommercialDetail> commercialDetails = null;
    @JsonProperty("totalReceivables")
    private Double totalReceivables;
    @JsonProperty("totalPayables")
    private Integer totalPayables;
    @JsonProperty("commercialsApplied")
    private List<String> commercialsApplied = null;
    @JsonProperty("totalFare")
    private Integer totalFare;
    @JsonProperty("entityCommercials")
    private List<BrmsClientEntityCommercial> entityCommercials = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("passengerType")
    public String getPassengerType() {
        return passengerType;
    }

    @JsonProperty("passengerType")
    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    @JsonProperty("commercialDetails")
    public List<BrmsClientCommercialDetail> getCommercialDetails() {
        return commercialDetails;
    }

    @JsonProperty("commercialDetails")
    public void setCommercialDetails(List<BrmsClientCommercialDetail> commercialDetails) {
        this.commercialDetails = commercialDetails;
    }

    @JsonProperty("totalReceivables")
    public Double getTotalReceivables() {
        return totalReceivables;
    }

    @JsonProperty("totalReceivables")
    public void setTotalReceivables(Double totalReceivables) {
        this.totalReceivables = totalReceivables;
    }

    @JsonProperty("totalPayables")
    public Integer getTotalPayables() {
        return totalPayables;
    }

    @JsonProperty("totalPayables")
    public void setTotalPayables(Integer totalPayables) {
        this.totalPayables = totalPayables;
    }

    @JsonProperty("commercialsApplied")
    public List<String> getCommercialsApplied() {
        return commercialsApplied;
    }

    @JsonProperty("commercialsApplied")
    public void setCommercialsApplied(List<String> commercialsApplied) {
        this.commercialsApplied = commercialsApplied;
    }

    @JsonProperty("totalFare")
    public Integer getTotalFare() {
        return totalFare;
    }

    @JsonProperty("totalFare")
    public void setTotalFare(Integer totalFare) {
        this.totalFare = totalFare;
    }

    @JsonProperty("entityCommercials")
    public List<BrmsClientEntityCommercial> getEntityCommercials() {
        return entityCommercials;
    }

    @JsonProperty("entityCommercials")
    public void setEntityCommercials(List<BrmsClientEntityCommercial> entityCommercials) {
        this.entityCommercials = entityCommercials;
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
