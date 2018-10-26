package com.coxandkings.travel.operations.model.core;

import java.io.Serializable;

public class OpsCommercialHead implements Serializable {
    private String id;
    private String groupName;
    private Boolean includeInClient;
    private Boolean retentionAllowed;
    private String type;
    private String value;
    private Boolean deleted;

    public OpsCommercialHead() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getIncludeInClient() {
        return includeInClient;
    }

    public void setIncludeInClient(Boolean includeInClient) {
        this.includeInClient = includeInClient;
    }

    public Boolean getRetentionAllowed() {
        return retentionAllowed;
    }

    public void setRetentionAllowed(Boolean retentionAllowed) {
        this.retentionAllowed = retentionAllowed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.id = value;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
