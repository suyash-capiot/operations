package com.coxandkings.travel.operations.resource.timelimitbooking;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"bufferDays",
"bufferHrs"
})
public class MDMBufferDaysInfo {

@JsonProperty("bufferDays")
private Integer bufferDays;
@JsonProperty("bufferHrs")
private Integer bufferHrs;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("bufferDays")
public Integer getBufferDays() {
return bufferDays;
}

@JsonProperty("bufferDays")
public void setBufferDays(Integer bufferDays) {
this.bufferDays = bufferDays;
}

@JsonProperty("bufferHrs")
public Integer getBufferHrs() {
return bufferHrs;
}

@JsonProperty("bufferHrs")
public void setBufferHrs(Integer bufferHrs) {
this.bufferHrs = bufferHrs;
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