
package com.coxandkings.travel.operations.resource.partpaymentmonitor;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "durationType"
})
public class PaymentDue {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("durationType")
    private String durationType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("durationType")
    public String getDurationType() {
        return durationType;
    }

    @JsonProperty("durationType")
    public void setDurationType(String durationType) {
        this.durationType = durationType;
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
