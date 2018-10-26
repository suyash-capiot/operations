
package com.coxandkings.travel.operations.resource.fullcancellation.clientKPI;

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
    "kpi",
    "businessProcess",
    "function",
    "_id",
    "kpiMeasure"
})
public class KpiDefinition {

    @JsonProperty("kpi")
    private String kpi;
    @JsonProperty("businessProcess")
    private String businessProcess;
    @JsonProperty("function")
    private String function;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("kpiMeasure")
    private KpiMeasure kpiMeasure;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("kpi")
    public String getKpi() {
        return kpi;
    }

    @JsonProperty("kpi")
    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    @JsonProperty("businessProcess")
    public String getBusinessProcess() {
        return businessProcess;
    }

    @JsonProperty("businessProcess")
    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    @JsonProperty("function")
    public String getFunction() {
        return function;
    }

    @JsonProperty("function")
    public void setFunction(String function) {
        this.function = function;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("kpiMeasure")
    public KpiMeasure getKpiMeasure() {
        return kpiMeasure;
    }

    @JsonProperty("kpiMeasure")
    public void setKpiMeasure(KpiMeasure kpiMeasure) {
        this.kpiMeasure = kpiMeasure;
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
