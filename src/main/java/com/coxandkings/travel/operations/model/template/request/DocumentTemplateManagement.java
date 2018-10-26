
package com.coxandkings.travel.operations.model.template.request;

import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "header",
    "dtmInput",
    "dynamicVariables"
})
public class DocumentTemplateManagement {

    @JsonProperty("header")
    private Header header;
    @JsonProperty("dtmInput")
    private TemplateInfo templateInfo;
    @JsonProperty("dynamicVariables")
    private List<DynamicVariables> dynamicVariables;

    public DocumentTemplateManagement(TemplateInfo templateInfo, List<DynamicVariables> dynamicVariables, String userId, String transactionId) {
        this.header = new Header(userId, transactionId);
        this.templateInfo = templateInfo;
        this.dynamicVariables = dynamicVariables;
    }

    @JsonProperty("header")
    public Header getHeader() {
        return header;
    }

    @JsonProperty("header")
    public void setHeader(Header header) {
        this.header = header;
    }

    public TemplateInfo getTemplateInfo() {
        return templateInfo;
    }

    public void setTemplateInfo(TemplateInfo templateInfo) {
        this.templateInfo = templateInfo;
    }

//    public DynamicVariables getDynamicVariables() {
//        return dynamicVariables;
//    }
//
//    public void setDynamicVariables(DynamicVariables dynamicVariables) {
//        this.dynamicVariables = dynamicVariables;
//    }


    public List<DynamicVariables> getDynamicVariables() {
        return dynamicVariables;
    }

    public void setDynamicVariables(List<DynamicVariables> dynamicVariables) {
        this.dynamicVariables = dynamicVariables;
    }
}
