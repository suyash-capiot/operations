
package com.coxandkings.travel.operations.model.template.request;

import com.coxandkings.travel.operations.resource.email.DynamicVariables;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cnk.documenttemplatemanagement.DocumentTemplateManagement"
})
public class TemplateObject {

    @JsonProperty("cnk.documenttemplatemanagement.DocumentTemplateManagement")
    private DocumentTemplateManagement documentTemplateManagement;

    public TemplateObject(TemplateInfo templateInfo, List<DynamicVariables> dynamicVariablesList , String userId, String transactionId) {
        this.documentTemplateManagement = new DocumentTemplateManagement(templateInfo, dynamicVariablesList, userId, transactionId);
    }

    @JsonProperty("cnk.documenttemplatemanagement.DocumentTemplateManagement")
    public DocumentTemplateManagement getDocumentTemplateManagement() {
        return documentTemplateManagement;
    }

    @JsonProperty("cnk.documenttemplatemanagement.DocumentTemplateManagement")
    public void setDocumentTemplateManagement(DocumentTemplateManagement documentTemplateManagement) {
        this.documentTemplateManagement = documentTemplateManagement;
    }
}
