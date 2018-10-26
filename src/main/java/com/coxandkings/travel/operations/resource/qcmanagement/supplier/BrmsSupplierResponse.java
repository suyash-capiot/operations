
package com.coxandkings.travel.operations.resource.qcmanagement.supplier;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "advancedDefinition",
    "journeyDetails",
    "ruleFlowName",
    "selectedRow",
    "commonElements",
    "commercialHead"
})
public class BrmsSupplierResponse {

    @JsonProperty("advancedDefinition")
    private BrmsSupplierAdvancedDefinition advancedDefinition;
    @JsonProperty("journeyDetails")
    private List<BrmsSupplierJourneyDetail> BRMSJourneyDetails = null;
    @JsonProperty("ruleFlowName")
    private String ruleFlowName;
    @JsonProperty("selectedRow")
    private String selectedRow;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("advancedDefinition")
    public BrmsSupplierAdvancedDefinition getAdvancedDefinition() {
        return advancedDefinition;
    }

    @JsonProperty("advancedDefinition")
    public void setAdvancedDefinition(BrmsSupplierAdvancedDefinition advancedDefinition) {
        this.advancedDefinition = advancedDefinition;
    }

    @JsonProperty("journeyDetails")
    public List<BrmsSupplierJourneyDetail> getBRMSJourneyDetails() {
        return BRMSJourneyDetails;
    }

    @JsonProperty("journeyDetails")
    public void setBRMSJourneyDetails(List<BrmsSupplierJourneyDetail> BRMSJourneyDetails) {
        this.BRMSJourneyDetails = BRMSJourneyDetails;
    }

    @JsonProperty("ruleFlowName")
    public String getRuleFlowName() {
        return ruleFlowName;
    }

    @JsonProperty("ruleFlowName")
    public void setRuleFlowName(String ruleFlowName) {
        this.ruleFlowName = ruleFlowName;
    }

    @JsonProperty("selectedRow")
    public String getSelectedRow() {
        return selectedRow;
    }

    @JsonProperty("selectedRow")
    public void setSelectedRow(String selectedRow) {
        this.selectedRow = selectedRow;
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
