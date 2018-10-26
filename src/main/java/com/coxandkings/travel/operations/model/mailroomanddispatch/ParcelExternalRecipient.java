package com.coxandkings.travel.operations.model.mailroomanddispatch;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class ParcelExternalRecipient extends BaseParcelRecipientDetails {
    private String passengerName;
    private String landLineNo;
    private String mobileNo;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getLandLineNo() {
        return landLineNo;
    }

    public void setLandLineNo(String landLineNo) {
        this.landLineNo = landLineNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
