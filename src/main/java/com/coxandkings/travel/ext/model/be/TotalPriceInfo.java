
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalPrice",
    "taxes",
    "currencyCode",
    "receivables",
    "discounts",
    "incentives",
    "companyTaxes",
})
public class TotalPriceInfo implements Serializable
{

    @JsonProperty("totalPrice")
    private String totalPrice;
    @JsonProperty("taxes")
    private Taxes taxes;
    @JsonProperty("currencyCode")
    private String currencyCode;
    
    @JsonProperty("discounts") 
    private Discounts discounts;

    @JsonProperty("incentives")
    private Incentives incentives;
    
    @JsonProperty("companyTaxes")
    private CompanyTaxes companyTaxes;
    
    @JsonProperty("receivables")
    private Receivables receivables;
    
    private final static long serialVersionUID = 2374367749626837835L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TotalPriceInfo() {
    }

    /**
     * 
     * @param totalPrice
     * @param taxes
     * @param currencyCode
     */
    public TotalPriceInfo(String totalPrice, Taxes taxes, String currencyCode) {
        super();
        this.totalPrice = totalPrice;
        this.taxes = taxes;
        this.currencyCode = currencyCode;
    }

    @JsonProperty("totalPrice")
    public String getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("totalPrice")
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonProperty("taxes")
    public Taxes getTaxes() {
        return taxes;
    }

    @JsonProperty("taxes")
    public void setTaxes(Taxes taxes) {
        this.taxes = taxes;
    }

    @JsonProperty("currencyCode")
    public String getCurrencyCode() {
        return currencyCode;
    }

    @JsonProperty("currencyCode")
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TotalPriceInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("totalPrice");
        sb.append('=');
        sb.append(((this.totalPrice == null)?"<null>":this.totalPrice));
        sb.append(',');
        sb.append("taxes");
        sb.append('=');
        sb.append(((this.taxes == null)?"<null>":this.taxes));
        sb.append(',');
        sb.append("currencyCode");
        sb.append('=');
        sb.append(((this.currencyCode == null)?"<null>":this.currencyCode));
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
        result = ((result* 31)+((this.taxes == null)? 0 :this.taxes.hashCode()));
        result = ((result* 31)+((this.totalPrice == null)? 0 :this.totalPrice.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TotalPriceInfo) == false) {
            return false;
        }
        TotalPriceInfo rhs = ((TotalPriceInfo) other);
        return ((((this.taxes == rhs.taxes)||((this.taxes!= null)&&this.taxes.equals(rhs.taxes)))&&((this.totalPrice == rhs.totalPrice)||((this.totalPrice!= null)&&this.totalPrice.equals(rhs.totalPrice))))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))));
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

	public CompanyTaxes getCompanyTaxes() {
		return companyTaxes;
	}

	public void setCompanyTaxes(CompanyTaxes companyTaxes) {
		this.companyTaxes = companyTaxes;
	}

	public Receivables getReceivables() {
		return receivables;
	}

	public void setReceivables(Receivables receivables) {
		this.receivables = receivables;
	}

}
