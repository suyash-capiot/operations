package com.coxandkings.travel.operations.resource.amendentitycommercial;

public class CommercialHead {

    private String commercialEntityID;
    private String commercialName;
    private String mdmRuleId;

    public CommercialHead(String commercialEntityID, String commercialName, String mdmRuleId) {
        this.commercialEntityID = commercialEntityID;
        this.commercialName = commercialName;
        this.mdmRuleId = mdmRuleId;
    }

    public String getMdmRuleId() {
        return mdmRuleId;
    }

    public void setMdmRuleId(String mdmRuleId) {
        this.mdmRuleId = mdmRuleId;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public String getCommercialEntityID() {
        return commercialEntityID;
    }

    public void setCommercialEntityID(String commercialEntityID) {
        this.commercialEntityID = commercialEntityID;
    }

}
