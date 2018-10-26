package com.coxandkings.travel.operations.resource.managedocumentation.documentmaster;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "documentType",
    "templateName",
    "assetId"
})
public class UploadDetail {

    @JsonProperty("documentType")
    private String documentType;
    @JsonProperty("templateName")
    private String templateName;
    @JsonProperty("assetId")
    private String assetId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("documentType")
    public String getDocumentType() {
        return documentType;
    }

    @JsonProperty("documentType")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @JsonProperty("templateName")
    public String getTemplateName() {
        return templateName;
    }

    @JsonProperty("templateName")
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @JsonProperty("assetId")
    public String getAssetId() {
        return assetId;
    }

    @JsonProperty("assetId")
    public void setAssetId(String assetId) {
        this.assetId = assetId;
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
