package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "documentSetting",
    "triggerEvent"
})
public class DocumentHandlingGrid {

    @JsonProperty("documentSetting")
    private List<DocumentSetting> documentSetting = null;
    @JsonProperty("triggerEvent")
    private TriggerEvent triggerEvent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("documentSetting")
    public List<DocumentSetting> getDocumentSetting() {
        return documentSetting;
    }

    @JsonProperty("documentSetting")
    public void setDocumentSetting(List<DocumentSetting> documentSetting) {
        this.documentSetting = documentSetting;
    }

    @JsonProperty("triggerEvent")
    public TriggerEvent getTriggerEvent() {
        return triggerEvent;
    }

    @JsonProperty("triggerEvent")
    public void setTriggerEvent(TriggerEvent triggerEvent) {
        this.triggerEvent = triggerEvent;
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
