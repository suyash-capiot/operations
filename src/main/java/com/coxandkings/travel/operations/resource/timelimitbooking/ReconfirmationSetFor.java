
package com.coxandkings.travel.operations.resource.timelimitbooking;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "DaysOrMonths",
    "count"
})
public class ReconfirmationSetFor {

    @JsonProperty("DaysOrMonths")
    private String daysOrMonths;
    @JsonProperty("count")
    private Integer count;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("DaysOrMonths")
    public String getDaysOrMonths() {
        return daysOrMonths;
    }

    @JsonProperty("DaysOrMonths")
    public void setDaysOrMonths(String daysOrMonths) {
        this.daysOrMonths = daysOrMonths;
    }

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
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
