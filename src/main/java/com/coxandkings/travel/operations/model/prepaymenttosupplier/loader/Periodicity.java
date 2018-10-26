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
        "repeatFreq",
        "halfYearly",
        "quarterly",
        "weekly",
        "fortnightly"
})
public class Periodicity {

    @JsonProperty("repeatFreq")
    private String repeatFreq;
    @JsonProperty("halfYearly")
    private List<HalfYearly> halfYearly = null;
    @JsonProperty("quarterly")
    private List<Quarterly> quarterly = null;
    @JsonProperty("weekly")
    private Weekly weekly;
    @JsonProperty("fortnightly")
    private List<Fortnightly> fortnightly = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("repeatFreq")
    public String getRepeatFreq() {
        return repeatFreq;
    }

    @JsonProperty("repeatFreq")
    public void setRepeatFreq(String repeatFreq) {
        this.repeatFreq = repeatFreq;
    }

    @JsonProperty("halfYearly")
    public List<HalfYearly> getHalfYearly() {
        return halfYearly;
    }

    @JsonProperty("halfYearly")
    public void setHalfYearly(List<HalfYearly> halfYearly) {
        this.halfYearly = halfYearly;
    }

    @JsonProperty("quarterly")
    public List<Quarterly> getQuarterly() {
        return quarterly;
    }

    @JsonProperty("quarterly")
    public void setQuarterly(List<Quarterly> quarterly) {
        this.quarterly = quarterly;
    }

    @JsonProperty("weekly")
    public Weekly getWeekly() {
        return weekly;
    }

    @JsonProperty("weekly")
    public void setWeekly(Weekly weekly) {
        this.weekly = weekly;
    }

    @JsonProperty("fortnightly")
    public List<Fortnightly> getFortnightly() {
        return fortnightly;
    }

    @JsonProperty("fortnightly")
    public void setFortnightly(List<Fortnightly> fortnightly) {
        this.fortnightly = fortnightly;
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