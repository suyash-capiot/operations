package com.coxandkings.travel.ext.model.finance.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassengerDetails {
    private String passengerName;
    private String passengerID;
    private String passengerTitle;
    private Boolean isLeadPassenger;
    private String passengerType;

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(String passengerID) {
        this.passengerID = passengerID;
    }

    public String getPassengerTitle() {
        return passengerTitle;
    }

    public void setPassengerTitle(String passengerTitle) {
        this.passengerTitle = passengerTitle;
    }

    public Boolean getLeadPassenger() {
        return isLeadPassenger;
    }

    public void setLeadPassenger(Boolean leadPassenger) {
        isLeadPassenger = leadPassenger;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PassengerDetails other = (PassengerDetails) obj;
        if (passengerID == null) {
            if (other.passengerID != null)
                return false;
        } else if (!passengerID.equals(other.passengerID))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((passengerID == null) ? 0 : passengerID.hashCode());
        return result;
    }

}
