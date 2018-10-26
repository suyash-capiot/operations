package com.coxandkings.travel.operations.service.reconfirmation.common;

public class SupplierConfiguration {


    private String reconfirmationCutOff;
    private String durationType;
    private Integer duration;
    private String supplierName;
    private String supplierId;
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

    public String getSupplierName( ) {
        return supplierName;
    }

    public void setSupplierName( String supplierName ) {
        this.supplierName = supplierName;
    }

    public String getSupplierId( ) {
        return supplierId;
    }

    public void setSupplierId( String supplierId ) {
        this.supplierId = supplierId;
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
