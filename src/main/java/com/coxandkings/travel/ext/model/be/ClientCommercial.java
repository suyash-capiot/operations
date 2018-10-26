
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "additionalCommercialDetails",
    "entityName",
    "fixedCommercialDetails",
    "retentionCommercialDetails",
    "commercialType",
    "markUpCommercialDetails"
})
public class ClientCommercial implements Serializable
{

    @JsonProperty("additionalCommercialDetails")
    private Object additionalCommercialDetails;
    @JsonProperty("entityName")
    private String entityName;
    @JsonProperty("fixedCommercialDetails")
    private Object fixedCommercialDetails;
    @JsonProperty("retentionCommercialDetails")
    private Object retentionCommercialDetails;
    @JsonProperty("commercialType")
    private String commercialType;
    @JsonProperty("markUpCommercialDetails")
    private MarkUpCommercialDetails markUpCommercialDetails;

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;
    @JsonProperty("commercialAmount")
    private String commercialAmount;
    @JsonProperty("commercialName")
    private String commercialName;
    @JsonProperty("mdmRuleID")
    private String mdmRuleID;
    
    @JsonProperty("commercialCalculationPercentage")
    private String commercialCalculationPercentage;
    @JsonProperty("commercialCalculationAmount")
    private String commercialCalculationAmount;
    @JsonProperty("commercialFareComponent")
    private String commercialFareComponent;
    @JsonProperty("retentionPercentage")
    private String retentionPercentage;
    @JsonProperty("retentionAmountPercentage")
    private String retentionAmountPercentage;
    @JsonProperty("remainingPercentageAmount")
    private String remainingPercentageAmount;
    @JsonProperty("remainingAmount")
    private String remainingAmount;
    
    
    @JsonProperty("companyFlag")
    private Boolean companyFlag;


    private final static long serialVersionUID = -1505312057610293880L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ClientCommercial() {
    }

    /**
     * 
     * @param additionalCommercialDetails
     * @param entityName
     * @param fixedCommercialDetails
     * @param retentionCommercialDetails
     * @param commercialType
     * @param markUpCommercialDetails
     */
    public ClientCommercial(Object additionalCommercialDetails, String entityName, Object fixedCommercialDetails, Object retentionCommercialDetails, String commercialType, MarkUpCommercialDetails markUpCommercialDetails) {
        super();
        this.additionalCommercialDetails = additionalCommercialDetails;
        this.entityName = entityName;
        this.fixedCommercialDetails = fixedCommercialDetails;
        this.retentionCommercialDetails = retentionCommercialDetails;
        this.commercialType = commercialType;
        this.markUpCommercialDetails = markUpCommercialDetails;
    }

    @JsonProperty("mdmRuleID")
    public String getMdmRuleID() {
        return mdmRuleID;
    }

    @JsonProperty("mdmRuleID")
    public void setMdmRuleID(String mdmRuleID) {
        this.mdmRuleID = mdmRuleID;
    }


    @JsonProperty("additionalCommercialDetails")
    public Object getAdditionalCommercialDetails() {
        return additionalCommercialDetails;
    }

    @JsonProperty("additionalCommercialDetails")
    public void setAdditionalCommercialDetails(Object additionalCommercialDetails) {
        this.additionalCommercialDetails = additionalCommercialDetails;
    }

    @JsonProperty("entityName")
    public String getEntityName() {
        return entityName;
    }

    @JsonProperty("entityName")
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    @JsonProperty("fixedCommercialDetails")
    public Object getFixedCommercialDetails() {
        return fixedCommercialDetails;
    }

    @JsonProperty("fixedCommercialDetails")
    public void setFixedCommercialDetails(Object fixedCommercialDetails) {
        this.fixedCommercialDetails = fixedCommercialDetails;
    }

    @JsonProperty("retentionCommercialDetails")
    public Object getRetentionCommercialDetails() {
        return retentionCommercialDetails;
    }

    @JsonProperty("retentionCommercialDetails")
    public void setRetentionCommercialDetails(Object retentionCommercialDetails) {
        this.retentionCommercialDetails = retentionCommercialDetails;
    }

    @JsonProperty("commercialType")
    public String getCommercialType() {
        return commercialType;
    }

    @JsonProperty("commercialType")
    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    @JsonProperty("markUpCommercialDetails")
    public MarkUpCommercialDetails getMarkUpCommercialDetails() {
        return markUpCommercialDetails;
    }

    @JsonProperty("markUpCommercialDetails")
    public void setMarkUpCommercialDetails(MarkUpCommercialDetails markUpCommercialDetails) {
        this.markUpCommercialDetails = markUpCommercialDetails;
    }

    @JsonProperty("commercialCurrency")
    public String getCommercialCurrency() {
        return commercialCurrency;
    }

    @JsonProperty("commercialCurrency")
    public void setCommercialCurrency(String commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
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

    public Boolean getCompanyFlag() {
        return companyFlag;
    }

    public void setCompanyFlag(Boolean companyFlag) {
        this.companyFlag = companyFlag;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ClientCommercial.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("additionalCommercialDetails");
        sb.append('=');
        sb.append(((this.additionalCommercialDetails == null)?"<null>":this.additionalCommercialDetails));
        sb.append(',');
        sb.append("entityName");
        sb.append('=');
        sb.append(((this.entityName == null)?"<null>":this.entityName));
        sb.append(',');
        sb.append("fixedCommercialDetails");
        sb.append('=');
        sb.append(((this.fixedCommercialDetails == null)?"<null>":this.fixedCommercialDetails));
        sb.append(',');
        sb.append("retentionCommercialDetails");
        sb.append('=');
        sb.append(((this.retentionCommercialDetails == null)?"<null>":this.retentionCommercialDetails));
        sb.append(',');
        sb.append("commercialType");
        sb.append('=');
        sb.append(((this.commercialType == null)?"<null>":this.commercialType));
        sb.append(',');
        sb.append("markUpCommercialDetails");
        sb.append('=');
        sb.append(((this.markUpCommercialDetails == null)?"<null>":this.markUpCommercialDetails));
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
        result = ((result* 31)+((this.additionalCommercialDetails == null)? 0 :this.additionalCommercialDetails.hashCode()));
        result = ((result* 31)+((this.entityName == null)? 0 :this.entityName.hashCode()));
        result = ((result* 31)+((this.fixedCommercialDetails == null)? 0 :this.fixedCommercialDetails.hashCode()));
        result = ((result* 31)+((this.retentionCommercialDetails == null)? 0 :this.retentionCommercialDetails.hashCode()));
        result = ((result* 31)+((this.commercialType == null)? 0 :this.commercialType.hashCode()));
        result = ((result* 31)+((this.markUpCommercialDetails == null)? 0 :this.markUpCommercialDetails.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ClientCommercial) == false) {
            return false;
        }
        ClientCommercial rhs = ((ClientCommercial) other);
        return (((((((this.additionalCommercialDetails == rhs.additionalCommercialDetails)||((this.additionalCommercialDetails!= null)&&this.additionalCommercialDetails.equals(rhs.additionalCommercialDetails)))&&((this.entityName == rhs.entityName)||((this.entityName!= null)&&this.entityName.equals(rhs.entityName))))&&((this.fixedCommercialDetails == rhs.fixedCommercialDetails)||((this.fixedCommercialDetails!= null)&&this.fixedCommercialDetails.equals(rhs.fixedCommercialDetails))))&&((this.retentionCommercialDetails == rhs.retentionCommercialDetails)||((this.retentionCommercialDetails!= null)&&this.retentionCommercialDetails.equals(rhs.retentionCommercialDetails))))&&((this.commercialType == rhs.commercialType)||((this.commercialType!= null)&&this.commercialType.equals(rhs.commercialType))))&&((this.markUpCommercialDetails == rhs.markUpCommercialDetails)||((this.markUpCommercialDetails!= null)&&this.markUpCommercialDetails.equals(rhs.markUpCommercialDetails))));
    }

	public String getCommercialCalculationPercentage() {
		return commercialCalculationPercentage;
	}

	public void setCommercialCalculationPercentage(String commercialCalculationPercentage) {
		this.commercialCalculationPercentage = commercialCalculationPercentage;
	}

	public String getCommercialCalculationAmount() {
		return commercialCalculationAmount;
	}

	public void setCommercialCalculationAmount(String commercialCalculationAmount) {
		this.commercialCalculationAmount = commercialCalculationAmount;
	}

	public String getCommercialFareComponent() {
		return commercialFareComponent;
	}

	public void setCommercialFareComponent(String commercialFareComponent) {
		this.commercialFareComponent = commercialFareComponent;
	}

	public String getRetentionPercentage() {
		return retentionPercentage;
	}

	public void setRetentionPercentage(String retentionPercentage) {
		this.retentionPercentage = retentionPercentage;
	}

	public String getRetentionAmountPercentage() {
		return retentionAmountPercentage;
	}

	public void setRetentionAmountPercentage(String retentionAmountPercentage) {
		this.retentionAmountPercentage = retentionAmountPercentage;
	}

	public String getRemainingPercentageAmount() {
		return remainingPercentageAmount;
	}

	public void setRemainingPercentageAmount(String remainingPercentageAmount) {
		this.remainingPercentageAmount = remainingPercentageAmount;
	}

	public String getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(String remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

}
