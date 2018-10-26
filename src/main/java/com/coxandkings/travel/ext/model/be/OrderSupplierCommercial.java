
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "commercialCurrency",
    "commercialType",
    "commercialAmount",
        "commercialName",
        "suppCommId"
})
public class OrderSupplierCommercial implements Serializable
{

    @JsonProperty("supplierID")
    private String supplierID;
    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("commercialAmount")
    private String commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("suppCommId")
    private String suppCommId;
    @JsonProperty("isEligible")
    private boolean isEligible;

    private final static long serialVersionUID = -6708090397906950524L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OrderSupplierCommercial() {
    }

    /**
     * 
     * @param commercialCurrency
     * @param commercialType
     * @param commercialAmount
     * @param commercialName
     * @param suppCommId
     */
    public OrderSupplierCommercial(String commercialCurrency, String commercialType, String commercialAmount, String commercialName, String suppCommId, String supplierID) {
        super();
        this.supplierID = supplierID;
        this.commercialCurrency = commercialCurrency;
        this.commercialType = commercialType;
        this.commercialAmount = commercialAmount;
        this.commercialName = commercialName;
        this.suppCommId = suppCommId;
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
    public String getCommercialAmount() {
        return commercialAmount;
    }

    @JsonProperty("commercialAmount")
    public void setCommercialAmount(String commercialAmount) {
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

    @JsonProperty("suppCommId")
    public String getSuppCommId() {
        return suppCommId;
    }

    @JsonProperty("suppCommId")
    public void setSuppCommId(String suppCommId) {
        this.suppCommId = suppCommId;
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
        sb.append(OrderSupplierCommercial.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("commercialCurrency");
        sb.append('=');
        sb.append(((this.commercialCurrency == null)?"<null>":this.commercialCurrency));
        sb.append(',');
        sb.append("commercialType");
        sb.append('=');
        sb.append(((this.commercialType == null)?"<null>":this.commercialType));
        sb.append(',');
        sb.append("supplierID");
        sb.append('=');
        sb.append(((this.supplierID == null)?"<null>":this.supplierID));
        sb.append(',');
        sb.append("commercialAmount");
        sb.append('=');
        sb.append(((this.commercialAmount == null)?"<null>":this.commercialAmount));
        sb.append(',');
        sb.append("commercialName");
        sb.append('=');
        sb.append(((this.commercialName == null)?"<null>":this.commercialName));
        sb.append(',');
        sb.append("suppCommId");
        sb.append('=');
        sb.append(((this.suppCommId == null) ? "<null>" : this.suppCommId));
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
        result = ((result* 31)+((this.commercialType == null)? 0 :this.commercialType.hashCode()));
        result = ((result* 31)+((this.commercialAmount == null)? 0 :this.commercialAmount.hashCode()));
        result = ((result* 31)+((this.commercialCurrency == null)? 0 :this.commercialCurrency.hashCode()));
        result = ((result* 31)+((this.commercialName == null)? 0 :this.commercialName.hashCode()));
        result = ((result * 31) + ((this.suppCommId == null) ? 0 : this.suppCommId.hashCode()));
        result = ((result * 31) + ((this.supplierID == null) ? 0 : this.supplierID.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderSupplierCommercial) == false) {
            return false;
        }
        OrderSupplierCommercial rhs = ((OrderSupplierCommercial) other);
        return (((((this.commercialType == rhs.commercialType)||((this.commercialType!= null)&&this.commercialType.equals(rhs.commercialType)))&&((this.commercialAmount == rhs.commercialAmount)||((this.commercialAmount!= null)&&this.commercialAmount.equals(rhs.commercialAmount))))&&((this.commercialCurrency == rhs.commercialCurrency)||((this.commercialCurrency!= null)&&this.commercialCurrency.equals(rhs.commercialCurrency))))&&((this.commercialName == rhs.commercialName)||((this.commercialName!= null)&&this.commercialName.equals(rhs.commercialName))));
    }

    @JsonProperty("isEligible")
	public boolean isEligible() {
		return isEligible;
	}

    @JsonProperty("isEligible")
	public void setEligible(boolean isEligible) {
		this.isEligible = isEligible;
	}

}
