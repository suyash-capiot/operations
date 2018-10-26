
package com.coxandkings.travel.operations.resource.user.role;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "functionName",
    "_id",
    "dataAccess",
    "screens",
    "RoutePath"
})
public class Function implements Serializable
{

    @JsonProperty("functionName")
    private String functionName;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("dataAccess")
    private List<Object> dataAccess = null;
    @JsonProperty("screens")
    private List<Screen> screens = null;
    @JsonProperty("RoutePath")
    private List<String> routePath = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8078931201653601422L;

    @JsonProperty("functionName")
    public String getFunctionName() {
        return functionName;
    }

    @JsonProperty("functionName")
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("dataAccess")
    public List<Object> getDataAccess() {
        return dataAccess;
    }

    @JsonProperty("dataAccess")
    public void setDataAccess(List<Object> dataAccess) {
        this.dataAccess = dataAccess;
    }

    @JsonProperty("screens")
    public List<Screen> getScreens() {
        return screens;
    }

    @JsonProperty("screens")
    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }

    @JsonProperty("RoutePath")
    public List<String> getRoutePath() {
        return routePath;
    }

    @JsonProperty("RoutePath")
    public void setRoutePath(List<String> routePath) {
        this.routePath = routePath;
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
