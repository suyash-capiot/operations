package com.coxandkings.travel.operations.service.reconfirmation.common;

public class ClientConfiguration {

    private String reconfirmationCutOff;
    private String durationType;
    private Integer duration;
    private String reconfirmationToBeSentTo;
    private String configurationFor;

    public String getReconfirmationCutOff( ) {
        return reconfirmationCutOff;
    }

    public void setReconfirmationCutOff( String reconfirmationCutOff ) {
        this.reconfirmationCutOff = reconfirmationCutOff;
    }

    public String getDurationType( ) {
        return durationType;
    }

    public void setDurationType( String durationType ) {
        this.durationType = durationType;
    }

    public Integer getDuration( ) {
        return duration;
    }

    public void setDuration( Integer duration ) {
        this.duration = duration;
    }

    public String getReconfirmationToBeSentTo( ) {
        return reconfirmationToBeSentTo;
    }

    public void setReconfirmationToBeSentTo( String reconfirmationToBeSentTo ) {
        this.reconfirmationToBeSentTo = reconfirmationToBeSentTo;
    }

    public String getConfigurationFor( ) {
        return configurationFor;
    }

    public void setConfigurationFor( String configurationFor ) {
        this.configurationFor = configurationFor;
    }
}
