
package com.coxandkings.travel.operations.model.core;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "paragraph",
    "subTitle"
})
public class OpsSubSection {

    @JsonProperty("paragraph")
    private OpsParagraph paragraph;
    @JsonProperty("subTitle")
    private String subTitle;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("paragraph")
    public OpsParagraph getParagraph() {
        return paragraph;
    }

    @JsonProperty("paragraph")
    public void setParagraph(OpsParagraph paragraph) {
        this.paragraph = paragraph;
    }

    @JsonProperty("subTitle")
    public String getSubTitle() {
        return subTitle;
    }

    @JsonProperty("subTitle")
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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
