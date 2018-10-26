package com.coxandkings.travel.operations.zmock.resource;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cliendId",
        "totalRevenueForPeriod",
        "totalGrossProfit",
        "totalOutstandingWithAgeing"
})
public class ClientFinancialBackgroundInfo {

    @JsonProperty("cliendId")
    private String cliendId;
    @JsonProperty("totalRevenueForPeriod")
    private String totalRevenueForPeriod;
    @JsonProperty("totalGrossProfit")
    private String totalGrossProfit;
    @JsonProperty("totalOutstandingWithAgeing")
    private String totalOutstandingWithAgeing;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("cliendId")
    public String getCliendId() {
        return cliendId;
    }

    @JsonProperty("cliendId")
    public void setCliendId(String cliendId) {
        this.cliendId = cliendId;
    }

    @JsonProperty("totalRevenueForPeriod")
    public String getTotalRevenueForPeriod() {
        return totalRevenueForPeriod;
    }

    @JsonProperty("totalRevenueForPeriod")
    public void setTotalRevenueForPeriod(String totalRevenueForPeriod) {
        this.totalRevenueForPeriod = totalRevenueForPeriod;
    }

    @JsonProperty("totalGrossProfit")
    public String getTotalGrossProfit() {
        return totalGrossProfit;
    }

    @JsonProperty("totalGrossProfit")
    public void setTotalGrossProfit(String totalGrossProfit) {
        this.totalGrossProfit = totalGrossProfit;
    }

    @JsonProperty("totalOutstandingWithAgeing")
    public String getTotalOutstandingWithAgeing() {
        return totalOutstandingWithAgeing;
    }

    @JsonProperty("totalOutstandingWithAgeing")
    public void setTotalOutstandingWithAgeing(String totalOutstandingWithAgeing) {
        this.totalOutstandingWithAgeing = totalOutstandingWithAgeing;
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

