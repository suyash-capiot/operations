package com.coxandkings.travel.operations.model.mailroomanddispatch;

import javax.persistence.Entity;

@Entity
public class ExternalRecipient extends BaseRecipientDetails{
    private String passengerName;
    private String landmark;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
