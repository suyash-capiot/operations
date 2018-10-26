package com.coxandkings.travel.operations.model.managedocumentation;

public class ModeOfCommunication {

    private boolean isCommModeCourier;
    private boolean isCommModeSMS;
    private boolean isCommModeEmail;
    private String courier;
    private String email;
    private String sms;

    public boolean isCommModeCourier() {
        return isCommModeCourier;
    }

    public void setCommModeCourier(boolean commModeCourier) {
        isCommModeCourier = commModeCourier;
    }

    public boolean isCommModeSMS() {
        return isCommModeSMS;
    }

    public void setCommModeSMS(boolean commModeSMS) {
        isCommModeSMS = commModeSMS;
    }

    public boolean isCommModeEmail() {
        return isCommModeEmail;
    }

    public void setCommModeEmail(boolean commModeEmail) {
        isCommModeEmail = commModeEmail;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }
}
