package com.coxandkings.travel.operations.resource.mailroomMaster;

import com.coxandkings.travel.operations.enums.mailroomanddispatch.ParcelType;
import com.coxandkings.travel.operations.model.mailroomanddispatch.BaseParcelRecipientDetails;

import java.util.List;

public class ParcelCreationForOutboundIdResource  {
    List<String> dispatchUniqueId;
    private String parcelId;
    private Double weight;
    private ParcelType parcelType;
    private BaseParcelRecipientDetails recipientDetailsList;


    public List<String> getDispatchUniqueId() {
        return dispatchUniqueId;
    }

    public void setDispatchUniqueId(List<String> dispatchUniqueId) {
        this.dispatchUniqueId = dispatchUniqueId;
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

   /* public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }*/
}
