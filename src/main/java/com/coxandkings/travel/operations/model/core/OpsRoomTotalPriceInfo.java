package com.coxandkings.travel.operations.model.core;

import com.coxandkings.travel.ext.model.be.Discounts;
import com.coxandkings.travel.ext.model.be.Incentives;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpsRoomTotalPriceInfo implements Serializable {

    @JsonProperty("taxes")
    private OpsTaxes opsTaxes;

    @JsonProperty("roomTotalPrice")
    private String roomTotalPrice;

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("receivables")
    private OpsReceivables receivables;
    
    @JsonProperty("companyTaxes")
    private OpsCompanyTaxes companyTaxes;
    
    @JsonProperty("discounts")
    private Discounts discounts;
    
    @JsonProperty("incentives")
    private Incentives incentives;

    public OpsReceivables getReceivables() {
        return receivables;
    }

    public void setReceivables(OpsReceivables receivables) {
        this.receivables = receivables;
    }

    public OpsRoomTotalPriceInfo() {
    }

    public OpsTaxes getOpsTaxes() {
        return opsTaxes;
    }

    public void setOpsTaxes(OpsTaxes opsTaxes) {
        this.opsTaxes = opsTaxes;
    }

    @JsonProperty("roomTotalPrice")
    public void setRoomTotalPrice(String roomTotalPrice) {
        this.roomTotalPrice = roomTotalPrice;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }


    public String getRoomTotalPrice() {
        return roomTotalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsRoomTotalPriceInfo that = (OpsRoomTotalPriceInfo) o;
        return Objects.equals(opsTaxes, that.opsTaxes) &&
                Objects.equals(roomTotalPrice, that.roomTotalPrice) &&
                Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {

        return Objects.hash(opsTaxes, roomTotalPrice, currencyCode);
    }

	public OpsCompanyTaxes getCompanyTaxes() {
		return companyTaxes;
	}

	public void setCompanyTaxes(OpsCompanyTaxes companyTaxes) {
		this.companyTaxes = companyTaxes;
	}

	public Discounts getDiscounts() {
		return discounts;
	}

	public void setDiscounts(Discounts discounts) {
		this.discounts = discounts;
	}

	public Incentives getIncentives() {
		return incentives;
	}

	public void setIncentives(Incentives incentives) {
		this.incentives = incentives;
	}
}
