
package com.coxandkings.travel.operations.resource.fullcancellation.clientKPI;

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
    "measure",
    "targetKPI",
    "currency",
    "achievingPeriod"
})
public class KpiMeasure {

    @JsonProperty("measure")
    private String measure;
    @JsonProperty("targetKPI")
    private String targetKPI;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("achievingPeriod")
    private AchievingPeriod achievingPeriod;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("measure")
    public String getMeasure() {
        return measure;
    }

    @JsonProperty("measure")
    public void setMeasure(String measure) {
        this.measure = measure;
    }

    @JsonProperty("targetKPI")
    public String getTargetKPI() {
        return targetKPI;
    }

    @JsonProperty("targetKPI")
    public void setTargetKPI(String targetKPI) {
        this.targetKPI = targetKPI;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("achievingPeriod")
    public AchievingPeriod getAchievingPeriod() {
        return achievingPeriod;
    }

    @JsonProperty("achievingPeriod")
    public void setAchievingPeriod(AchievingPeriod achievingPeriod) {
        this.achievingPeriod = achievingPeriod;
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
