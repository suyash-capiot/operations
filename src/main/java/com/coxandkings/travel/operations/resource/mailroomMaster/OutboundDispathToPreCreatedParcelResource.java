package com.coxandkings.travel.operations.resource.mailroomMaster;

import java.util.List;

public class OutboundDispathToPreCreatedParcelResource {
    private String parcelId;
    private List<String> savedDispatchId;

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public List<String> getSavedDispatchId() {
        return savedDispatchId;
    }

    public void setSavedDispatchId(List<String> savedDispatchId) {
        this.savedDispatchId = savedDispatchId;
    }
}
