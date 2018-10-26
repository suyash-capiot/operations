
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commercialCalculationAmount",
    "commercialFareComponent",
    "commercialCalculationPercentage",
    "commercialInitialAmount",
    "commercialTotalAmount",
    "commercialCurrency",
    "commercialType",
    "commercialAmount",
    "commercialName",
        "mdmRuleID",
    "fareBreakUp"
})
public class SupplierCommercial implements Serializable
{

    @JsonProperty("commercialCalculationAmount")
    private Double commercialCalculationAmount;

    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;

    @JsonProperty("commercialCalculationPercentage")
    private Double commercialCalculationPercentage;

    @JsonProperty("commercialInitialAmount")
    private Double commercialInitialAmount;

    @JsonProperty("commercialTotalAmount")
    private Double commercialTotalAmount;

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;

    @JsonProperty("commercialType")
    private String commercialType;

    @JsonProperty("commercialAmount")
    private Double commercialAmount;

    @JsonProperty("commercialName")
    private String commercialName;

    @JsonProperty("mdmRuleID")
    private String mdmRuleID;

    @JsonProperty("supplierID")
    private String supplierID;

    @JsonProperty("fareBreakUp")
    private FareBreakUp fareBreakUp;

    private final static long serialVersionUID = -6107588031120406529L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public SupplierCommercial() {
    }

    /**
     * 
     * @param commercialCalculationAmount
     * @param commercialFareComponent
     * @param commercialCalculationPercentage
     * @param commercialInitialAmount
     * @param commercialTotalAmount
     * @param commercialCurrency
     * @param commercialType
     * @param commercialAmount
     * @param commercialName
     * @param fareBreakUp
     */
    public SupplierCommercial(Double commercialCalculationAmount, String commercialFareComponent, Double commercialCalculationPercentage,
                      Double commercialInitialAmount, Double commercialTotalAmount, String commercialCurrency, String commercialType,
                      Double commercialAmount, String commercialName, FareBreakUp fareBreakUp, String mdmRuleID, String supplierID) {
        super();
        this.commercialCalculationAmount = commercialCalculationAmount;
        this.commercialFareComponent = commercialFareComponent;
        this.commercialCalculationPercentage = commercialCalculationPercentage;
        this.commercialInitialAmount = commercialInitialAmount;
        this.commercialTotalAmount = commercialTotalAmount;
        this.commercialCurrency = commercialCurrency;
        this.commercialType = commercialType;
        this.commercialAmount = commercialAmount;
        this.commercialName = commercialName;
        this.fareBreakUp = fareBreakUp;
        this.mdmRuleID = mdmRuleID;
        this.supplierID = supplierID;
    }

    @JsonProperty("commercialCalculationAmount")
    public Double getCommercialCalculationAmount() {
        return commercialCalculationAmount;
    }

    @JsonProperty("commercialCalculationAmount")
    public void setCommercialCalculationAmount(Double commercialCalculationAmount) {
        this.commercialCalculationAmount = commercialCalculationAmount;
    }

    @JsonProperty("commercialFareComponent")
    public String getCommercialFareComponent() {
        return commercialFareComponent;
    }

    @JsonProperty("commercialFareComponent")
    public void setCommercialFareComponent(String commercialFareComponent) {
        this.commercialFareComponent = commercialFareComponent;
    }

    @JsonProperty("commercialCalculationPercentage")
    public Double getCommercialCalculationPercentage() {
        return commercialCalculationPercentage;
    }

    @JsonProperty("commercialCalculationPercentage")
    public void setCommercialCalculationPercentage(Double commercialCalculationPercentage) {
        this.commercialCalculationPercentage = commercialCalculationPercentage;
    }

    @JsonProperty("commercialInitialAmount")
    public Double getCommercialInitialAmount() {
        return commercialInitialAmount;
    }

    @JsonProperty("commercialInitialAmount")
    public void setCommercialInitialAmount(Double commercialInitialAmount) {
        this.commercialInitialAmount = commercialInitialAmount;
    }

    @JsonProperty("commercialTotalAmount")
    public Double getCommercialTotalAmount() {
        return commercialTotalAmount;
    }

    @JsonProperty("commercialTotalAmount")
    public void setCommercialTotalAmount(Double commercialTotalAmount) {
        this.commercialTotalAmount = commercialTotalAmount;
    }

    @JsonProperty("commercialCurrency")
    public String getCommercialCurrency() {
        return commercialCurrency;
    }

    @JsonProperty("commercialCurrency")
    public void setCommercialCurrency(String commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    @JsonProperty("commercialType")
    public String getCommercialType() {
        return commercialType;
    }

    @JsonProperty("commercialType")
    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    @JsonProperty("commercialAmount")
    public Double getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    @JsonProperty("commercialName")
    public String getCommercialName() {
        return commercialName;
    }

    @JsonProperty("commercialName")
    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    @JsonProperty("fareBreakUp")
    public FareBreakUp getFareBreakUp() {
        return fareBreakUp;
    }

    @JsonProperty("fareBreakUp")
    public void setFareBreakUp(FareBreakUp fareBreakUp) {
        this.fareBreakUp = fareBreakUp;
    }

    @JsonProperty("mdmRuleID")
    public String getMdmRuleID() {
        return mdmRuleID;
    }

    @JsonProperty("mdmRuleID")
    public void setMdmRuleID(String mdmRuleID) {
        this.mdmRuleID = mdmRuleID;
    }

    @JsonProperty("supplierID")
    public String getSupplierID() {
        return supplierID;
    }

    @JsonProperty("supplierID")
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SupplierCommercial.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("commercialCalculationAmount");
        sb.append('=');
        sb.append(((this.commercialCalculationAmount == null)?"<null>":this.commercialCalculationAmount));
        sb.append(',');
        sb.append("commercialFareComponent");
        sb.append('=');
        sb.append(((this.commercialFareComponent == null)?"<null>":this.commercialFareComponent));
        sb.append(',');
        sb.append("commercialCalculationPercentage");
        sb.append('=');
        sb.append(((this.commercialCalculationPercentage == null)?"<null>":this.commercialCalculationPercentage));
        sb.append(',');
        sb.append("commercialInitialAmount");
        sb.append('=');
        sb.append(((this.commercialInitialAmount == null)?"<null>":this.commercialInitialAmount));
        sb.append(',');
        sb.append("commercialTotalAmount");
        sb.append('=');
        sb.append(((this.commercialTotalAmount == null)?"<null>":this.commercialTotalAmount));
        sb.append(',');
        sb.append("commercialCurrency");
        sb.append('=');
        sb.append(((this.commercialCurrency == null)?"<null>":this.commercialCurrency));
        sb.append(',');
        sb.append("Ops");
        sb.append('=');
        sb.append(((this.supplierID == null)?"<null>":this.supplierID));
        sb.append(',');
        sb.append("commercialType");
        sb.append('=');
        sb.append(((this.commercialType == null)?"<null>":this.commercialType));
        sb.append(',');
        sb.append("commercialAmount");
        sb.append('=');
        sb.append(((this.commercialAmount == null)?"<null>":this.commercialAmount));
        sb.append(',');
        sb.append("commercialName");
        sb.append('=');
        sb.append(((this.commercialName == null)?"<null>":this.commercialName));
        sb.append(',');
        sb.append("fareBreakUp");
        sb.append('=');
        sb.append(((this.fareBreakUp == null)?"<null>":this.fareBreakUp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.commercialCalculationAmount == null)? 0 :this.commercialCalculationAmount.hashCode()));
        result = ((result* 31)+((this.commercialFareComponent == null)? 0 :this.commercialFareComponent.hashCode()));
        result = ((result* 31)+((this.commercialCalculationPercentage == null)? 0 :this.commercialCalculationPercentage.hashCode()));
        result = ((result* 31)+((this.commercialInitialAmount == null)? 0 :this.commercialInitialAmount.hashCode()));
        result = ((result* 31)+((this.commercialTotalAmount == null)? 0 :this.commercialTotalAmount.hashCode()));
        result = ((result* 31)+((this.commercialCurrency == null)? 0 :this.commercialCurrency.hashCode()));
        result = ((result* 31)+((this.commercialType == null)? 0 :this.commercialType.hashCode()));
        result = ((result* 31)+((this.commercialAmount == null)? 0 :this.commercialAmount.hashCode()));
        result = ((result* 31)+((this.commercialName == null)? 0 :this.commercialName.hashCode()));
        result = ((result* 31)+((this.fareBreakUp == null)? 0 :this.fareBreakUp.hashCode()));
        result = ((result* 31)+((this.supplierID == null)? 0 :this.supplierID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SupplierCommercial) == false) {
            return false;
        }
        SupplierCommercial rhs = ((SupplierCommercial) other);
        return (((((((((((this.commercialCalculationAmount == rhs.commercialCalculationAmount)||((this.commercialCalculationAmount!= null)&&this.commercialCalculationAmount.equals(rhs.commercialCalculationAmount)))&&((this.commercialFareComponent == rhs.commercialFareComponent)||((this.commercialFareComponent!= null)&&this.commercialFareComponent.equals(rhs.commercialFareComponent))))&&((this.commercialCalculationPercentage == rhs.commercialCalculationPercentage)||((this.commercialCalculationPercentage!= null)&&this.commercialCalculationPercentage.equals(rhs.commercialCalculationPercentage))))&&((this.commercialInitialAmount == rhs.commercialInitialAmount)||((this.commercialInitialAmount!= null)&&this.commercialInitialAmount.equals(rhs.commercialInitialAmount))))&&((this.commercialTotalAmount == rhs.commercialTotalAmount)||((this.commercialTotalAmount!= null)&&this.commercialTotalAmount.equals(rhs.commercialTotalAmount))))&&((this.commercialCurrency == rhs.commercialCurrency)||((this.commercialCurrency!= null)&&this.commercialCurrency.equals(rhs.commercialCurrency))))&&((this.commercialType == rhs.commercialType)||((this.commercialType!= null)&&this.commercialType.equals(rhs.commercialType))))&&((this.commercialAmount == rhs.commercialAmount)||((this.commercialAmount!= null)&&this.commercialAmount.equals(rhs.commercialAmount))))&&((this.commercialName == rhs.commercialName)||((this.commercialName!= null)&&this.commercialName.equals(rhs.commercialName))))&&((this.fareBreakUp == rhs.fareBreakUp)||((this.fareBreakUp!= null)&&this.fareBreakUp.equals(rhs.fareBreakUp))));
    }

}
