
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "clientID",
    "parentClientID",
    "commercialEntityType",
    "companyFlag",
    "commercialCurrency",
    "commercialType",
    "commercialEntityID",
    "commercialAmount",
    "commercialName"
})
public class OrderClientCommercial implements Serializable
{

    @JsonProperty("clientID")
    private String clientID;
    @JsonProperty("parentClientID")
    private String parentClientID;
    @JsonProperty("commercialEntityType")
    private String commercialEntityType;
    @JsonProperty("companyFlag")
    private Boolean companyFlag;
    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("commercialEntityID")
    private String commercialEntityID;
    @JsonProperty("commercialAmount")
    private String commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("clientCommId")
    private String clientCommId;
    @JsonProperty("isEligible")
    private boolean isEligible;
    

    private final static long serialVersionUID = -6073622402921393398L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OrderClientCommercial() {
    }

    /**
     * 
     * @param clientID
     * @param parentClientID
     * @param commercialEntityType
     * @param companyFlag
     * @param commercialCurrency
     * @param commercialType
     * @param commercialEntityID
     * @param commercialAmount
     * @param commercialName
     */
    public OrderClientCommercial(String clientID, String parentClientID, String commercialEntityType, Boolean companyFlag,
                                 String commercialCurrency, String commercialType, String commercialEntityID,
                                 String commercialAmount, String commercialName, String clientCommId) {
        super();
        this.clientID = clientID;
        this.parentClientID = parentClientID;
        this.commercialEntityType = commercialEntityType;
        this.companyFlag = companyFlag;
        this.commercialCurrency = commercialCurrency;
        this.commercialType = commercialType;
        this.commercialEntityID = commercialEntityID;
        this.commercialAmount = commercialAmount;
        this.commercialName = commercialName;
        this.clientCommId = clientCommId;
    }

    @JsonProperty("clientID")
    public String getClientID() {
        return clientID;
    }

    @JsonProperty("clientID")
    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonProperty("parentClientID")
    public String getParentClientID() {
        return parentClientID;
    }

    @JsonProperty("parentClientID")
    public void setParentClientID(String parentClientID) {
        this.parentClientID = parentClientID;
    }

    @JsonProperty("commercialEntityType")
    public String getCommercialEntityType() {
        return commercialEntityType;
    }

    @JsonProperty("commercialEntityType")
    public void setCommercialEntityType(String commercialEntityType) {
        this.commercialEntityType = commercialEntityType;
    }

    @JsonProperty("companyFlag")
    public Boolean getCompanyFlag() {
        return companyFlag;
    }

    @JsonProperty("companyFlag")
    public void setCompanyFlag(Boolean companyFlag) {
        this.companyFlag = companyFlag;
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

    @JsonProperty("commercialEntityID")
    public String getCommercialEntityID() {
        return commercialEntityID;
    }

    @JsonProperty("commercialEntityID")
    public void setCommercialEntityID(String commercialEntityID) {
        this.commercialEntityID = commercialEntityID;
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

    public String getClientCommId() {
        return clientCommId;
    }

    public void setClientCommId(String clientCommId) {
        this.clientCommId = clientCommId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OrderClientCommercial.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("clientID");
        sb.append('=');
        sb.append(((this.clientID == null)?"<null>":this.clientID));
        sb.append(',');
        sb.append("parentClientID");
        sb.append('=');
        sb.append(((this.parentClientID == null)?"<null>":this.parentClientID));
        sb.append(',');
        sb.append("commercialEntityType");
        sb.append('=');
        sb.append(((this.commercialEntityType == null)?"<null>":this.commercialEntityType));
        sb.append(',');
        sb.append("companyFlag");
        sb.append('=');
        sb.append(((this.companyFlag == null)?"<null>":this.companyFlag));
        sb.append(',');
        sb.append("commercialCurrency");
        sb.append('=');
        sb.append(((this.commercialCurrency == null)?"<null>":this.commercialCurrency));
        sb.append(',');
        sb.append("commercialType");
        sb.append('=');
        sb.append(((this.commercialType == null)?"<null>":this.commercialType));
        sb.append(',');
        sb.append("commercialEntityID");
        sb.append('=');
        sb.append(((this.commercialEntityID == null)?"<null>":this.commercialEntityID));
        sb.append(',');
        sb.append("commercialAmount");
        sb.append('=');
        sb.append(((this.commercialAmount == null)?"<null>":this.commercialAmount));
        sb.append(',');
        sb.append("commercialName");
        sb.append('=');
        sb.append(((this.commercialName == null)?"<null>":this.commercialName));
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
        result = ((result* 31)+((this.clientID == null)? 0 :this.clientID.hashCode()));
        result = ((result* 31)+((this.parentClientID == null)? 0 :this.parentClientID.hashCode()));
        result = ((result* 31)+((this.commercialEntityType == null)? 0 :this.commercialEntityType.hashCode()));
        result = ((result* 31)+((this.companyFlag == null)? 0 :this.companyFlag.hashCode()));
        result = ((result* 31)+((this.commercialCurrency == null)? 0 :this.commercialCurrency.hashCode()));
        result = ((result* 31)+((this.commercialType == null)? 0 :this.commercialType.hashCode()));
        result = ((result* 31)+((this.commercialEntityID == null)? 0 :this.commercialEntityID.hashCode()));
        result = ((result* 31)+((this.commercialAmount == null)? 0 :this.commercialAmount.hashCode()));
        result = ((result* 31)+((this.commercialName == null)? 0 :this.commercialName.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderClientCommercial) == false) {
            return false;
        }
        OrderClientCommercial rhs = ((OrderClientCommercial) other);
        return ((((((((((this.clientID == rhs.clientID)||((this.clientID!= null)&&this.clientID.equals(rhs.clientID)))&&((this.parentClientID == rhs.parentClientID)||((this.parentClientID!= null)&&this.parentClientID.equals(rhs.parentClientID))))&&((this.commercialEntityType == rhs.commercialEntityType)||((this.commercialEntityType!= null)&&this.commercialEntityType.equals(rhs.commercialEntityType))))&&((this.companyFlag == rhs.companyFlag)||((this.companyFlag!= null)&&this.companyFlag.equals(rhs.companyFlag))))&&((this.commercialCurrency == rhs.commercialCurrency)||((this.commercialCurrency!= null)&&this.commercialCurrency.equals(rhs.commercialCurrency))))&&((this.commercialType == rhs.commercialType)||((this.commercialType!= null)&&this.commercialType.equals(rhs.commercialType))))&&((this.commercialEntityID == rhs.commercialEntityID)||((this.commercialEntityID!= null)&&this.commercialEntityID.equals(rhs.commercialEntityID))))&&((this.commercialAmount == rhs.commercialAmount)||((this.commercialAmount!= null)&&this.commercialAmount.equals(rhs.commercialAmount))))&&((this.commercialName == rhs.commercialName)||((this.commercialName!= null)&&this.commercialName.equals(rhs.commercialName))));
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
