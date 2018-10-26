package com.coxandkings.travel.operations.resource.mailroomMaster;

import java.util.List;

public class OutboundsDetachFromParcel {
    List<String> outboundIds;
    String parcelId;

    public List<String> getOutboundIds() {
        return outboundIds;
    }

    public void setOutboundIds(List<String> outboundIds) {
        this.outboundIds = outboundIds;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }
}
