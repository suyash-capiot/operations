package com.coxandkings.travel.operations.resource.manageofflinebooking;

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
    "ancillaryInfo"
})
public class AncillaryServices {

    @JsonProperty("ancillaryInfo")
    private List<AncillaryInfo> ancillaryInfo = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("ancillaryInfo")
    public List<AncillaryInfo> getAncillaryInfo() {
        return ancillaryInfo;
    }

    @JsonProperty("ancillaryInfo")
    public void setAncillaryInfo(List<AncillaryInfo> ancillaryInfo) {
        this.ancillaryInfo = ancillaryInfo;
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
