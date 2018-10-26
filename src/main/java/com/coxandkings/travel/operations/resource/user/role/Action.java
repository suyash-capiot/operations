

package com.coxandkings.travel.operations.resource.user.role;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "actionName",
    "_id",
    "permission"
})
public class Action implements Serializable
{

    @JsonProperty("actionName")
    private String actionName;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("permission")
    private Boolean permission;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6300369158669728783L;

    @JsonProperty("actionName")
    public String getActionName() {
        return actionName;
    }

    @JsonProperty("actionName")
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("permission")
    public Boolean getPermission() {
        return permission;
    }

    @JsonProperty("permission")
    public void setPermission(Boolean permission) {
        this.permission = permission;
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

