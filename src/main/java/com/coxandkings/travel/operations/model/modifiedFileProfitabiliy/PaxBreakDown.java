package com.coxandkings.travel.operations.model.modifiedFileProfitabiliy;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PaxBreakDown {
    private String leadPassengerName;
    private String passengerName;
    @Column(name = "no_of_adtls")
    private Integer noOfAdtls;
    @Column(name="no_ofchildren")
    private Integer noOfchildren;

    public String getLeadPassengerName() {
        return leadPassengerName;
    }

    public void setLeadPassengerName(String leadPassengerName) {
        this.leadPassengerName = leadPassengerName;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Integer getNoOfAdtls() {
        return noOfAdtls;
    }

    public void setNoOfAdtls(Integer noOfAdtls) {
        this.noOfAdtls = noOfAdtls;
    }

    public Integer getNoOfchildren() {
        return noOfchildren;
    }

    public void setNoOfchildren(Integer noOfchildren) {
        this.noOfchildren = noOfchildren;
    }
}
