package com.coxandkings.travel.operations.model.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpsPaxRoomClientCommercial implements Serializable {

    @JsonProperty("commercialCurrency")
    private String commercialCurrency;

    @JsonProperty("commercialType")
    private String commercialType;

    @JsonProperty("commercialAmount")
    private Double commercialAmount;

    @JsonProperty("commercialName")
    private String commercialName;

    //TODO: Booking2 engine not proving this flag
    @JsonProperty("companyFlag")
    private Boolean companyFlag;

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
    
    public String getCommercialCurrency() {
        return commercialCurrency;
    }

    public void setCommercialCurrency(String commercialCurrency) {
        this.commercialCurrency = commercialCurrency;
    }

    public String getMdmRuleID() {
        return mdmRuleID;
    }

    public void setMdmRuleID(String mdmRuleID) {
        this.mdmRuleID = mdmRuleID;
    }

    public String getCommercialType() {
        return commercialType;
    }

    public void setCommercialType(String commercialType) {
        this.commercialType = commercialType;
    }

    public Double getCommercialAmount() {
        return commercialAmount;
    }

    public void setCommercialAmount(Double commercialAmount) {
        this.commercialAmount = commercialAmount;
    }

    public String getCommercialName() {
        return commercialName;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsPaxRoomClientCommercial that = (OpsPaxRoomClientCommercial) o;
        return Objects.equals(commercialCurrency, that.commercialCurrency) &&
                Objects.equals(commercialType, that.commercialType) &&
                Objects.equals(commercialAmount, that.commercialAmount) &&
                Objects.equals(commercialName, that.commercialName) &&
                Objects.equals(companyFlag, that.companyFlag) &&
                Objects.equals(mdmRuleID, that.mdmRuleID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(commercialCurrency, commercialType, commercialAmount, commercialName, companyFlag, mdmRuleID);
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
