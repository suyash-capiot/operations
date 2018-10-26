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
        "weeklySchedule",
        "dates"
})
public class Weekly {

    @JsonProperty("weeklySchedule")
    private String weeklySchedule;
    @JsonProperty("dates")
    private List<Object> dates = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("weeklySchedule")
    public String getWeeklySchedule() {
        return weeklySchedule;
    }

    @JsonProperty("weeklySchedule")
    public void setWeeklySchedule(String weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }

    @JsonProperty("dates")
    public List<Object> getDates() {
        return dates;
    }

    @JsonProperty("dates")
    public void setDates(List<Object> dates) {
        this.dates = dates;
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