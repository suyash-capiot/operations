package com.coxandkings.travel.operations.resource.mailroomMaster;


import com.coxandkings.travel.operations.enums.mailroomanddispatch.ParcelType;
import com.coxandkings.travel.operations.model.mailroomanddispatch.BaseParcelRecipientDetails;
import com.coxandkings.travel.operations.resource.BaseResource;

public class CreateParcelResource extends BaseResource {

    private String parcelId;
    private Double weight;
    private ParcelType parcelType;
    private BaseParcelRecipientDetails recipientDetailsList;
    /*private String statusId;*/

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

   }
