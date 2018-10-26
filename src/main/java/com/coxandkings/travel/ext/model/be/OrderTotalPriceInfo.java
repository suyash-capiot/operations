
package com.coxandkings.travel.ext.model.be;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "totalPrice",
    "taxes",
    "currencyCode",
    "baseFare",
    "fees",
    "receivables",
    "paxTypeFares",
    "discounts",
    "incentives",
    "companyTaxes",
    "specialServiceRequests"
})
public class OrderTotalPriceInfo implements Serializable
{

    @JsonProperty("totalPrice")
    private String totalPrice;
    @JsonProperty("taxes")
    private Taxes taxes;
    @JsonProperty("currencyCode")
    private String currencyCode;
    @JsonProperty("baseFare")
    private BaseFare baseFare;
    @JsonProperty("fees")
    private Fees fees;
    @JsonProperty("receivables")
    private Receivables receivables;
    @JsonProperty("paxTypeFares")
    private List<PaxTypeFare> paxTypeFares = new ArrayList<PaxTypeFare>();

    @JsonProperty("discounts") //offerDetails
    private Discounts discounts;

    @JsonProperty("incentives")
    private Incentives incentives;
    
    @JsonProperty("companyTaxes")
    private CompanyTaxes companyTaxes;

    @JsonProperty("specialServiceRequests") //offerDetails
    private List<SpecialServiceRequest> specialServiceRequests;


    private final static long serialVersionUID = 4375858403895283439L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public OrderTotalPriceInfo() {
    }

    /**
     * 
     * @param baseFare
     * @param fees
     * @param totalPrice
     * @param taxes
     * @param receivables
     * @param currencyCode
     * @param paxTypeFares
     */
    public OrderTotalPriceInfo(String totalPrice, Taxes taxes, String currencyCode, BaseFare baseFare, Fees fees, Receivables receivables, List<PaxTypeFare> paxTypeFares,Discounts discounts,
                       List<SpecialServiceRequest> specialServiceRequests, CompanyTaxes companyTaxes,Incentives incentives) {
        super();
        this.totalPrice = totalPrice;
        this.taxes = taxes;
        this.currencyCode = currencyCode;
        this.baseFare = baseFare;
        this.fees = fees;
        this.receivables = receivables;
        this.paxTypeFares = paxTypeFares;
        this.discounts = discounts;
        this.specialServiceRequests = specialServiceRequests;
        this.companyTaxes = companyTaxes;
        this.incentives = incentives;
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

    @JsonProperty("baseFare")
    public BaseFare getBaseFare() {
        return baseFare;
    }

    @JsonProperty("baseFare")
    public void setBaseFare(BaseFare baseFare) {
        this.baseFare = baseFare;
    }

    @JsonProperty("fees")
    public Fees getFees() {
        return fees;
    }

    @JsonProperty("fees")
    public void setFees(Fees fees) {
        this.fees = fees;
    }

    @JsonProperty("receivables")
    public Receivables getReceivables() {
        return receivables;
    }

    @JsonProperty("receivables")
    public void setReceivables(Receivables receivables) {
        this.receivables = receivables;
    }

    @JsonProperty("paxTypeFares")
    public List<PaxTypeFare> getPaxTypeFares() {
        return paxTypeFares;
    }

    @JsonProperty("paxTypeFares")
    public void setPaxTypeFares(List<PaxTypeFare> paxTypeFares) {
        this.paxTypeFares = paxTypeFares;
    }

    @JsonProperty("discounts")
    public Discounts getDiscounts() {
        return discounts;
    }

    @JsonProperty("discounts")
    public void setDiscounts(Discounts discounts) {
        this.discounts = discounts;
    }

    @JsonProperty("specialServiceRequests")
    public List<SpecialServiceRequest> getSpecialServiceRequests() {
        return specialServiceRequests;
    }

    @JsonProperty("specialServiceRequests")
    public void setSpecialServiceRequests(List<SpecialServiceRequest> specialServiceRequests) {
        this.specialServiceRequests = specialServiceRequests;
    }

    @JsonProperty("companyTaxes")
    public CompanyTaxes getCompanyTaxes() {
        return companyTaxes;
    }

    @JsonProperty("companyTaxes")
    public void setCompanyTaxes(CompanyTaxes companyTaxes) {
        this.companyTaxes = companyTaxes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OrderTotalPriceInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        sb.append("baseFare");
        sb.append('=');
        sb.append(((this.baseFare == null)?"<null>":this.baseFare));
        sb.append(',');
        sb.append("fees");
        sb.append('=');
        sb.append(((this.fees == null)?"<null>":this.fees));
        sb.append(',');
        sb.append("receivables");
        sb.append('=');
        sb.append(((this.receivables == null)?"<null>":this.receivables));
        sb.append(',');
        sb.append("paxTypeFares");
        sb.append('=');
        sb.append(((this.paxTypeFares == null)?"<null>":this.paxTypeFares));
        sb.append(',');
        sb.append("discounts");
        sb.append('=');
        sb.append(((this.discounts == null)?"<null>":this.discounts));
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
        result = ((result* 31)+((this.baseFare == null)? 0 :this.baseFare.hashCode()));
        result = ((result* 31)+((this.fees == null)? 0 :this.fees.hashCode()));
        result = ((result* 31)+((this.totalPrice == null)? 0 :this.totalPrice.hashCode()));
        result = ((result* 31)+((this.taxes == null)? 0 :this.taxes.hashCode()));
        result = ((result* 31)+((this.receivables == null)? 0 :this.receivables.hashCode()));
        result = ((result* 31)+((this.currencyCode == null)? 0 :this.currencyCode.hashCode()));
        result = ((result* 31)+((this.paxTypeFares == null)? 0 :this.paxTypeFares.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderTotalPriceInfo) == false) {
            return false;
        }
        OrderTotalPriceInfo rhs = ((OrderTotalPriceInfo) other);
        return ((((((((this.baseFare == rhs.baseFare)||((this.baseFare!= null)&&this.baseFare.equals(rhs.baseFare)))&&((this.fees == rhs.fees)||((this.fees!= null)&&this.fees.equals(rhs.fees))))&&((this.totalPrice == rhs.totalPrice)||((this.totalPrice!= null)&&this.totalPrice.equals(rhs.totalPrice))))&&((this.taxes == rhs.taxes)||((this.taxes!= null)&&this.taxes.equals(rhs.taxes))))&&((this.receivables == rhs.receivables)||((this.receivables!= null)&&this.receivables.equals(rhs.receivables))))&&((this.currencyCode == rhs.currencyCode)||((this.currencyCode!= null)&&this.currencyCode.equals(rhs.currencyCode))))&&((this.paxTypeFares == rhs.paxTypeFares)||((this.paxTypeFares!= null)&&this.paxTypeFares.equals(rhs.paxTypeFares))));
    }

	public Incentives getIncentives() {
		return incentives;
	}

	public void setIncentives(Incentives incentives) {
		this.incentives = incentives;
	}

}
