
package com.coxandkings.travel.operations.resource.qcmanagement.client;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "commonElements",
        "advancedDefinition",
        "selectedRow",
        "ruleFlowName",
        "entityDetails",
        "journeyDetails",
        "tempStatus"
})
public class BRMSClientResponse {

    @JsonProperty("commonElements")
    private CommonElements commonElements;
    @JsonProperty("advancedDefinition")
    private BrmsClientAdvancedDefinition advancedDefinition;
    @JsonProperty("selectedRow")
    private String selectedRow;
    @JsonProperty("ruleFlowName")
    private String ruleFlowName;
    @JsonProperty("journeyDetails")
    private List<BrmsClientJourneyDetail> journeyDetails = null;
    @JsonProperty("tempStatus")
    private Boolean tempStatus;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("commonElements")
    public CommonElements getCommonElements() {
        return commonElements;
    }

    @JsonProperty("commonElements")
    public void setCommonElements(CommonElements commonElements) {
        this.commonElements = commonElements;
    }

    @JsonProperty("advancedDefinition")
    public BrmsClientAdvancedDefinition getAdvancedDefinition() {
        return advancedDefinition;
    }

    @JsonProperty("advancedDefinition")
    public void setAdvancedDefinition(BrmsClientAdvancedDefinition advancedDefinition) {
        this.advancedDefinition = advancedDefinition;
    }

    @JsonProperty("selectedRow")
    public String getSelectedRow() {
        return selectedRow;
    }

    @JsonProperty("selectedRow")
    public void setSelectedRow(String selectedRow) {
        this.selectedRow = selectedRow;
    }

    @JsonProperty("ruleFlowName")
    public String getRuleFlowName() {
        return ruleFlowName;
    }

    @JsonProperty("ruleFlowName")
    public void setRuleFlowName(String ruleFlowName) {
        this.ruleFlowName = ruleFlowName;
    }

    @JsonProperty("journeyDetails")
    public List<BrmsClientJourneyDetail> getJourneyDetails() {
        return journeyDetails;
    }

    @JsonProperty("journeyDetails")
    public void setJourneyDetails(List<BrmsClientJourneyDetail> journeyDetails) {
        this.journeyDetails = journeyDetails;
    }

    @JsonProperty("tempStatus")
    public Boolean getTempStatus() {
        return tempStatus;
    }

    @JsonProperty("tempStatus")
    public void setTempStatus(Boolean tempStatus) {
        this.tempStatus = tempStatus;
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
