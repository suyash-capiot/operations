
package com.coxandkings.travel.operations.resource.qcmanagement.supplier;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "passengerType",
    "commercialsApplied",
    "totalFare",
    "totalReceivables",
    "totalPayables",
    "fareBreakUp",
    "commercialDetails"
})
public class BrmsSupplierPassengerDetail {

    @JsonProperty("passengerType")
    private String passengerType;
    @JsonProperty("commercialsApplied")
    private List<String> commercialsApplied = null;
    @JsonProperty("totalFare")
    private Integer totalFare;
    @JsonProperty("totalReceivables")
    private Integer totalReceivables;
    @JsonProperty("totalPayables")
    private Integer totalPayables;
    @JsonProperty("commercialDetails")
    private List<BrmsSupplierCommercialDetail> commercialDetails = null;
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

    @JsonProperty("totalReceivables")
    public Integer getTotalReceivables() {
        return totalReceivables;
    }

    @JsonProperty("totalReceivables")
    public void setTotalReceivables(Integer totalReceivables) {
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

    @JsonProperty("commercialDetails")
    public List<BrmsSupplierCommercialDetail> getCommercialDetails() {
        return commercialDetails;
    }

    @JsonProperty("commercialDetails")
    public void setCommercialDetails(List<BrmsSupplierCommercialDetail> commercialDetails) {
        this.commercialDetails = commercialDetails;
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
