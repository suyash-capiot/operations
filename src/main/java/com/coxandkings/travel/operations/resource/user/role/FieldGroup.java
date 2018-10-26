
package com.coxandkings.travel.operations.resource.user.role;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fieldGroupName",
    "fieldGroupPath",
    "_id",
    "permission"
})
public class FieldGroup implements Serializable
{

    @JsonProperty("fieldGroupName")
    private String fieldGroupName;
    @JsonProperty("fieldGroupPath")
    private String fieldGroupPath;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("permission")
    private String permission;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -5222223648702456623L;

    @JsonProperty("fieldGroupName")
    public String getFieldGroupName() {
        return fieldGroupName;
    }

    @JsonProperty("fieldGroupName")
    public void setFieldGroupName(String fieldGroupName) {
        this.fieldGroupName = fieldGroupName;
    }

    @JsonProperty("fieldGroupPath")
    public String getFieldGroupPath() {
        return fieldGroupPath;
    }

    @JsonProperty("fieldGroupPath")
    public void setFieldGroupPath(String fieldGroupPath) {
        this.fieldGroupPath = fieldGroupPath;
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
    public String getPermission() {
        return permission;
    }

    @JsonProperty("permission")
    public void setPermission(String permission) {
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
