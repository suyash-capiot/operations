
package com.coxandkings.travel.operations.resource.fullcancellation;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "orderID",
    "orderCancellations"
})
public class ProductResource {

    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("orderCancellations")
    private List<OrderCancellationResource> orderCancellations = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    @JsonProperty("orderCancellations")
    public List<OrderCancellationResource> getOrderCancellations() {
        return orderCancellations;
    }

    @JsonProperty("orderCancellations")
    public void setOrderCancellations(List<OrderCancellationResource> orderCancellations) {
        this.orderCancellations = orderCancellations;
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
