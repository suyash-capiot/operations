
package com.coxandkings.travel.operations.resource.fullcancellation.clientKPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_id",
    "kpiRemarks",
    "entityId",
    "createdBy",
    "versionHistory",
    "versionNumber",
    "deleted",
    "lastUpdated",
    "createdAt",
    "kpiDefinition",
    "keyPerformanceIndicator",
    "__v"
})
public class ClientKPI {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("kpiRemarks")
    private String kpiRemarks;
    @JsonProperty("entityId")
    private String entityId;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("versionHistory")
    private List<Object> versionHistory = null;
    @JsonProperty("versionNumber")
    private Integer versionNumber;
    @JsonProperty("deleted")
    private Boolean deleted;
    @JsonProperty("lastUpdated")
    private String lastUpdated;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("kpiDefinition")
    private List<KpiDefinition> kpiDefinition = null;
    @JsonProperty("keyPerformanceIndicator")
    private KeyPerformanceIndicator keyPerformanceIndicator;
    @JsonProperty("__v")
    private Integer v;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("kpiRemarks")
    public String getKpiRemarks() {
        return kpiRemarks;
    }

    @JsonProperty("kpiRemarks")
    public void setKpiRemarks(String kpiRemarks) {
        this.kpiRemarks = kpiRemarks;
    }

    @JsonProperty("entityId")
    public String getEntityId() {
        return entityId;
    }

    @JsonProperty("entityId")
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @JsonProperty("createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonProperty("createdBy")
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty("versionHistory")
    public List<Object> getVersionHistory() {
        return versionHistory;
    }

    @JsonProperty("versionHistory")
    public void setVersionHistory(List<Object> versionHistory) {
        this.versionHistory = versionHistory;
    }

    @JsonProperty("versionNumber")
    public Integer getVersionNumber() {
        return versionNumber;
    }

    @JsonProperty("versionNumber")
    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    @JsonProperty("deleted")
    public Boolean getDeleted() {
        return deleted;
    }

    @JsonProperty("deleted")
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @JsonProperty("lastUpdated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("lastUpdated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @JsonProperty("createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty("kpiDefinition")
    public List<KpiDefinition> getKpiDefinition() {
        return kpiDefinition;
    }

    @JsonProperty("kpiDefinition")
    public void setKpiDefinition(List<KpiDefinition> kpiDefinition) {
        this.kpiDefinition = kpiDefinition;
    }

    @JsonProperty("keyPerformanceIndicator")
    public KeyPerformanceIndicator getKeyPerformanceIndicator() {
        return keyPerformanceIndicator;
    }

    @JsonProperty("keyPerformanceIndicator")
    public void setKeyPerformanceIndicator(KeyPerformanceIndicator keyPerformanceIndicator) {
        this.keyPerformanceIndicator = keyPerformanceIndicator;
    }

    @JsonProperty("__v")
    public Integer getV() {
        return v;
    }

    @JsonProperty("__v")
    public void setV(Integer v) {
        this.v = v;
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
