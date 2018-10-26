
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "entityName",
        "additionalCommercialDetails",
        "markUpCommercialDetails"
})
public class BrmsClientEntityCommercial {

    @JsonProperty("entityName")
    private String entityName;
    @JsonProperty("additionalCommercialDetails")
    private List<AdditionalCommercialDetail> additionalCommercialDetails = null;
    @JsonProperty("markUpCommercialDetails")
    private MarkUpCommercialDetails markUpCommercialDetails;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("entityName")
    public String getEntityName() {
        return entityName;
    }

    @JsonProperty("entityName")
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @JsonProperty("additionalCommercialDetails")
    public List<AdditionalCommercialDetail> getAdditionalCommercialDetails() {
        return additionalCommercialDetails;
    }

    @JsonProperty("additionalCommercialDetails")
    public void setAdditionalCommercialDetails(List<AdditionalCommercialDetail> additionalCommercialDetails) {
        this.additionalCommercialDetails = additionalCommercialDetails;
    }

    @JsonProperty("markUpCommercialDetails")
    public MarkUpCommercialDetails getMarkUpCommercialDetails() {
        return markUpCommercialDetails;
    }

    @JsonProperty("markUpCommercialDetails")
    public void setMarkUpCommercialDetails(MarkUpCommercialDetails markUpCommercialDetails) {
        this.markUpCommercialDetails = markUpCommercialDetails;
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
