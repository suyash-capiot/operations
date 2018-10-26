
package com.coxandkings.travel.operations.resource.user.role;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "screenName",
    "_id",
    "actions",
    "fieldGroups",
    "screenPermission"
})
public class Screen implements Serializable
{

    @JsonProperty("screenName")
    private String screenName;
    @JsonProperty("_id")
    private String id;
    @JsonProperty("actions")
    private List<Action> actions = null;
    @JsonProperty("fieldGroups")
    private List<FieldGroup> fieldGroups = null;
    @JsonProperty("screenPermission")
    private Boolean screenPermission;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -8850200486777423682L;

    @JsonProperty("screenName")
    public String getScreenName() {
        return screenName;
    }

    @JsonProperty("screenName")
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }

    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("actions")
    public List<Action> getActions() {
        return actions;
    }

    @JsonProperty("actions")
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @JsonProperty("fieldGroups")
    public List<FieldGroup> getFieldGroups() {
        return fieldGroups;
    }

    @JsonProperty("fieldGroups")
    public void setFieldGroups(List<FieldGroup> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }

    @JsonProperty("screenPermission")
    public Boolean getScreenPermission() {
        return screenPermission;
    }

    @JsonProperty("screenPermission")
    public void setScreenPermission(Boolean screenPermission) {
        this.screenPermission = screenPermission;
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
