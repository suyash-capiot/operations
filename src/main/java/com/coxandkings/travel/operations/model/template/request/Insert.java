
package com.coxandkings.travel.operations.model.template.request;

import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "out-identifier",
    "return-object",
    "entry-point",
    "object"
})
public class Insert {

    @JsonProperty("out-identifier")
    private String outIdentifier;
    @JsonProperty("return-object")
    private Boolean returnObject;
    @JsonProperty("entry-point")
    private String entryPoint;
    @JsonProperty("object")
    private TemplateObject templateObject;

    public Insert(TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables, String outIdentifier, Boolean entryPoint, String returnObject, String userId, String transactionId) {
        this.outIdentifier = outIdentifier;
        this.returnObject = entryPoint;
        this.entryPoint = returnObject;
        this.templateObject = new TemplateObject(templateInfo, dynamicVariables, userId, transactionId);
    }

    @JsonProperty("out-identifier")
    public String getOutIdentifier() {
        return outIdentifier;
    }

    @JsonProperty("out-identifier")
    public void setOutIdentifier(String outIdentifier) {
        this.outIdentifier = outIdentifier;
    }

    @JsonProperty("return-object")
    public Boolean getReturnObject() {
        return returnObject;
    }

    @JsonProperty("return-object")
    public void setReturnObject(Boolean returnObject) {
        this.returnObject = returnObject;
    }

    @JsonProperty("entry-point")
    public String getEntryPoint() {
        return entryPoint;
    }

    @JsonProperty("entry-point")
    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public TemplateObject getTemplateObject() {
        return templateObject;
    }

    public void setTemplateObject(TemplateObject templateObject) {
        this.templateObject = templateObject;
    }
}
