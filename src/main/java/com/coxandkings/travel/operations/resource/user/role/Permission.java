
package com.coxandkings.travel.operations.resource.user.role;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "businessProcess",
    "_id",
    "functions"
})
public class Permission implements Serializable
{

    @JsonProperty("businessProcess")
    private String businessProcess;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("functions")
    private List<Function> functions = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3512682797071596486L;

    @JsonProperty("businessProcess")
    public String getBusinessProcess() {
        return businessProcess;
    }

    @JsonProperty("businessProcess")
    public void setBusinessProcess(String businessProcess) {
        this.businessProcess = businessProcess;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("functions")
    public List<Function> getFunctions() {
        return functions;
    }

    @JsonProperty("functions")
    public void setFunctions(List<Function> functions) {
        this.functions = functions;
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
