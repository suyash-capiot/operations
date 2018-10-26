package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.ParcelType;
import com.coxandkings.travel.operations.model.mailroomanddispatch.BaseParcelRecipientDetails;
import com.coxandkings.travel.operations.model.mailroomanddispatch.OutboundDispatch;

import java.util.List;

public class ParcelResource {
    private String id;
    private String parcelId;
    private Double weight;
    private ParcelType parcelType;
    private BaseParcelRecipientDetails recipientDetailsList;
    private List<OutboundDispatch> outboundDispatches;

    public ParcelResource() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }

    public BaseParcelRecipientDetails getRecipientDetailsList() {
        return recipientDetailsList;
    }

    public void setRecipientDetailsList(BaseParcelRecipientDetails recipientDetailsList) {
        this.recipientDetailsList = recipientDetailsList;
    }

    public List<OutboundDispatch> getOutboundDispatches() {
        return outboundDispatches;
    }

    public void setOutboundDispatches(List<OutboundDispatch> outboundDispatches) {
        this.outboundDispatches = outboundDispatches;
    }
}
